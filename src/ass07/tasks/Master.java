package ass07.tasks;

import ass07.tasks.MainView;
import ass07.tasks.GameModel;
import ass07.tasks.PlayerModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Master extends Thread{

    private MainView view;
    private GameModel gameModel;
    private GenericSynchronizedFlag<Boolean> turnFlag;
    private GenericSynchronizedFlag<Integer> winnerFlag;
    private ExecutorService myExecutorService;
    private List<Future<PlayerModel>> futureList;

    public Master(GameModel model, MainView view, GenericSynchronizedFlag<Boolean> flag, GenericSynchronizedFlag<Integer> winnerflag){
        this.view = view;
        this.gameModel = model;
        this.turnFlag = flag;
        this.winnerFlag = winnerflag;
    }

    @Override
    public void run() {
    	while(this.turnFlag.getValue()){
    		this.gameModel.nextGameTurnCount(); // Incrementa il contatore dei turni
    		this.futureList = new ArrayList<>();
    		this.myExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);
    		long startTime = System.nanoTime();
    		this.gameModel.getPlayers().parallelStream()
    				.forEach(playerData -> this.futureList.add(this.myExecutorService.submit(new PlayerTask(playerData, this.gameModel, this.winnerFlag))));
    		List<PlayerModel> result = new ArrayList<>();
    		try {
    			for (Future<PlayerModel> future: futureList) {
    				if (future.get() != null) {
    					result.add(future.get());
    				}
    			}
    			// Versione funzionale del for each qui sopra
    			/*futureList.parallelStream().filter(future -> {
    				try {
						return future.get()!=null;
					} catch (Exception e) {
						e.printStackTrace();
					}
					return false;
    			}).forEach(future -> {
    				try {
						result.add(future.get());
					} catch (Exception e) {
						e.printStackTrace();
					}
    			});*/
    			this.myExecutorService.shutdown();
    			this.myExecutorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
    		} catch (InterruptedException | ExecutionException e) {
    			e.printStackTrace();
    		}
    		this.view.updateView(result, this.gameModel.getGameTurn(), (System.nanoTime()-startTime)/1000);
    		this.gameModel.updatePlayersReferences(result);
    		if(this.winnerFlag.getValue()>=0){
    			this.turnFlag.setValue(false);
    			this.view.updateWinner(this.winnerFlag.getValue(), result.size());
    		}
    		try {
    			Thread.sleep(200); // Pausa per rallentare l'output
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    }
}

package ass06.seeds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Master extends Thread {

	private SeedsModel model;
	private SeedsView view;
	private Flag stopFlag;
	private int nTasks;
	private long period;
	private int nAliveCells;
	private List<Future<Integer>> resultList;

	public Master(SeedsModel model, SeedsView view, Flag stopFlag, int frameRate, int nTasks) {
		this.model = model;
		this.view = view;
		this.stopFlag = stopFlag;
		this.nTasks = nTasks;
		this.period = 1000 / frameRate;
		this.nAliveCells = 0;
		this.resultList = new ArrayList<>();
	}

	public void run() {
		List<CalculateNextStateTask> tasksList = new ArrayList<CalculateNextStateTask>();
		int x0 = 0;
		int dx = this.model.getWidth() / this.nTasks;
		for(int i=0; i<this.nTasks-1; i++){
			tasksList.add(new CalculateNextStateTask(this.model, x0, 0, dx, this.model.getHeight()));
			x0 += dx;
		}
		tasksList.add(new CalculateNextStateTask(this.model, x0, 0, this.model.getWidth()-x0, this.model.getHeight()));
		ExecutorService myExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()+1);

		while(!this.stopFlag.isSet()) { // Continua a calcolare finché non viene fatto clic sul pulsante "Stop"						

			// Invia all'executor service il task
			tasksList.stream().forEach(t -> { this.resultList.add(myExecutorService.submit(t)); });				

			// Incrementa il contatore delle celle attive
			for (Future<Integer> f: this.resultList){
				try {
					this.nAliveCells += f.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			};
			this.resultList.clear();	
			this.model.swap(); // Passa dalla griglia corrente a quella dello stato successivo 
			this.view.update(this.nAliveCells); // Aggiorna la gui con le nuove celle
			try {
				Thread.sleep(this.period);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}			
	}
}

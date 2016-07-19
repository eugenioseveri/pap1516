package ass07.tasks;

import ass07.tasks.MainView;
import ass07.tasks.GameModel;
import ass07.tasks.Master;

public class Controller implements InputListener {

    private MainView view;
    private GameModel gameModel;
    private GenericSynchronizedFlag<Boolean> turnFlag;
    private GenericSynchronizedFlag<Integer> winnerFlag;

    public Controller(MainView view, GameModel gameModel){
        this.view = view;
        this.gameModel = gameModel;
        this.turnFlag = new GenericSynchronizedFlag<Boolean>(false);
        this.winnerFlag = new GenericSynchronizedFlag<Integer>(-1);
    }

    @Override
    public void started() {
        this.turnFlag.setValue(true);
        this.view.setMagicNumber(this.gameModel.getMagicNumber());
        new Master(this.gameModel, this.view, this.turnFlag, this.winnerFlag).start();
    }

    @Override
    public void stopped() {
        this.turnFlag.setValue(false);
    }

    @Override
    public void reset() {
        this.gameModel.initializeGame();
        this.winnerFlag = new GenericSynchronizedFlag<Integer>(-1);
    }
}

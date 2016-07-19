package ass07.tasks;

import ass07.tasks.GameModel;
import ass07.tasks.PlayerModel;

import java.util.concurrent.Callable;

public class PlayerTask implements Callable<PlayerModel>{

    private PlayerModel playerModel;
    private GameModel gameModel;
    private GenericSynchronizedFlag<Integer> winnerFlag;

    public PlayerTask (PlayerModel playerModel, GameModel gameModel, GenericSynchronizedFlag<Integer> winnerFlag){
        this.playerModel = playerModel;
        this.gameModel = gameModel;
        this.winnerFlag = winnerFlag;
    }

    @Override
    public PlayerModel call() throws Exception {

        if (this.winnerFlag.getValue()==-1) {
            int hint = this.gameModel.tryNumber(this.playerModel.getNextAttempt());
            if(hint==0) {
                this.winnerFlag.setValue(this.playerModel.getPlayerNumber());
            } else {
                this.playerModel.getHint(hint);
            }
        }
        return this.playerModel;
    }
}

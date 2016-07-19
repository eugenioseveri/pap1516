package ass07.tasks;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerModel {

    private int playerNumber;
    private int lowerBound;
    private int upperBound;
    private int lastNumber;

    public PlayerModel(int playerNumber){
        this.playerNumber = playerNumber;
        this.lowerBound = 0;
        this.upperBound = Integer.MAX_VALUE;
    }

    public int getPlayerNumber(){
        return this.playerNumber;
    }

    public int getNextAttempt(){
        this.lastNumber = ThreadLocalRandom.current().nextInt(this.lowerBound, this.upperBound);
        return this.lastNumber;
    }

    public void getHint(int hint){
        if(hint<0){
            this.upperBound = this.lastNumber;
        } else {
            this.lowerBound = this.lastNumber;
        }
    }

    public String log() {
        return "player-" + this.playerNumber +
        		"   Numero: " + this.lastNumber;
    }
}

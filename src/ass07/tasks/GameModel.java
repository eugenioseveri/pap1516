package ass07.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameModel {

    private int playersNumber;
    private int gameTurn;
    private int magicNumber;
    private List<PlayerModel> playersReferences;

    public GameModel (int playersNumber) {
        this.playersNumber = playersNumber;
        this.initializeGame();
    }

    public int getGameTurn(){
        return this.gameTurn;
    }

    public int getMagicNumber(){
        return this.magicNumber;
    }
    
    public List<PlayerModel> getPlayers(){
        return this.playersReferences;
    }

    public void initializeGame(){
        this.gameTurn = 0;
        this.magicNumber = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);
        this.playersReferences = new ArrayList<>();
        for (int i=0; i<this.playersNumber; i++){
            this.playersReferences.add(new PlayerModel(i));
        }
    }

    public void updatePlayersReferences(List<PlayerModel> updateList){
        this.playersReferences = updateList;
    }

    public int tryNumber(int triedNumber){
        return Integer.compare(this.magicNumber, triedNumber);
    }

    public void nextGameTurnCount(){
        this.gameTurn++;
    }

}

package ass07.actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
    	int playersNumber = 0; // Numero di giocatori (specificato come argomento)
		try {
			playersNumber = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Numero player non passato come parametro!");
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.out.println("Il numero di player deve essere un intero!");
			System.exit(1);
		}
    	
    	List<ActorRef> players = new ArrayList<>();
        ActorSystem system = ActorSystem.create("TheGame");
        for(int i=0; i<playersNumber; i++) {
            ActorRef player = system.actorOf(Props.create(PlayerActor.class), "player-"+i);
            players.add(player);
        }
        ActorRef oracleRef = system.actorOf(Props.create(OracleActor.class));
        oracleRef.tell(new InitializeMessage(players), ActorRef.noSender());
    }
}

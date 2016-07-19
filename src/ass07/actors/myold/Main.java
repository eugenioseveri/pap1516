package ass07.actors.myold;

import akka.actor.ActorSystem;
import akka.actor.Props;

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
		ActorSystem system = ActorSystem.create("GuessTheNumberSystem");
		system.actorOf(Props.create(Oracle.class, playersNumber));
	}
}

package ass07.actors.myold;

import java.util.concurrent.ThreadLocalRandom;

import akka.actor.UntypedActor;

public class Player extends UntypedActor {
	
	private int currentNumber;
	private int lowerBound;
	private int upperBound;
	
	@Override
	public void preStart() {
		this.currentNumber = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE); // Primo numero inizializzato a caso
		this.lowerBound = 0;
		this.upperBound = Integer.MAX_VALUE;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof Integer) {
			if((int)message==-1) {
				this.lowerBound = this.currentNumber;
				this.currentNumber = ThreadLocalRandom.current().nextInt(this.lowerBound, this.upperBound);
			} else if((int)message==11) {
				this.upperBound = this.currentNumber;
				this.currentNumber = ThreadLocalRandom.current().nextInt(this.lowerBound, this.upperBound);
			} else {
				unhandled(message);
			}
		} else if(message instanceof String) {
			if((String)message == "sob!") { // Partita persa
				System.out.println(getSelf().path().name() + " sob!");
			} else if((String)message == "won!") { // Partita vinta
				System.out.println(getSelf().path().name() + " won! " + this.currentNumber);
			} else if((String)message == "start") { // Inizio computazione
				getSender().tell(this.currentNumber, getSelf());
			} else {
				unhandled(message);
			}
		} else{
			unhandled(message);
		}
		
		/*if((String)message == "<") { // Il numero specificato è minore di quello da indovinare
			this.lowerBound = this.currentNumber;
			this.currentNumber = ThreadLocalRandom.current().nextInt(this.lowerBound, this.upperBound);
			getSender().tell(this.currentNumber, getSelf());
		} else if((String)message == ">") { // Il numero specificato è maggiore di quello da indovinare
			this.upperBound = this.currentNumber;
			this.currentNumber = ThreadLocalRandom.current().nextInt(this.lowerBound, this.upperBound);
			getSender().tell(this.currentNumber, getSelf());
		} else if((String)message == "sob!") { // Partita persa
			System.out.println(getSelf().path().name() + " sob!");
		} else if((String)message == "won!") { // Partita vinta
			System.out.println(getSelf().path().name() + " won! " + this.currentNumber);
		} else if((String)message == "start") { // Inizio computazione
			getSender().tell(this.currentNumber, getSelf());
		} else {
			unhandled(message);
		}*/

	}

}

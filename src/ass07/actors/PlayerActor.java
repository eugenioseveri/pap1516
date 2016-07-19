package ass07.actors;

import akka.actor.UntypedActor;

import java.util.concurrent.ThreadLocalRandom;

public class PlayerActor extends UntypedActor {
	
	private int currentNumber;
	private int lowerBound;
    private int upperBound;

    @Override
    public void preStart() {
    	this.currentNumber = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE-1); // Primo numero inizializzato a caso
    	this.lowerBound = 0;
    	this.upperBound = Integer.MAX_VALUE-1;
    }
    
    @Override
    public void onReceive(Object message) throws Exception { // Le tipologie di messaggio sono ordinate per probabilità di arrivo
        if (message instanceof HintMessage) { // Il numero inviato è sbagliato: ridimensionare i bound in base al suggerimento
            if(((HintMessage) message).getMessage()==1) {
            	this.upperBound = this.currentNumber;
            } else {
            	this.lowerBound = this.currentNumber;
            }
            getSender().tell(new TryNumberMessage(tryNumber(this.lowerBound, this.upperBound-1)), getSelf());
        } else if (message instanceof EndGameMessage) { // Fine partita (perché uno dei player ha indovinato il numero
            if(((EndGameMessage)message).getMessage()) { //Partita vinta
            	System.out.println(getSelf().path().name() + " won! " + this.currentNumber);
            } else { // Partita persa
            	System.out.println(getSelf().path().name() + " sob!");
            }
            getSender().tell(message, getSelf()); // Notifica all'oracolo di aver terminato (riutilizza l'ultimo messaggio)
            getContext().stop(getSelf());
        } else if (message instanceof StartMessage) { // Inizio partita
            getSender().tell(new TryNumberMessage(tryNumber(this.lowerBound, this.upperBound-1)), getSelf());
        } else {
        	unhandled(message);
        }
    }

    private int tryNumber(int lowerBound, int upperBound) {
    	this.currentNumber = ThreadLocalRandom.current().nextInt(lowerBound, upperBound);
        System.out.println(getSelf().path().name() + "  Numero: " + currentNumber);
        return this.currentNumber;
    }
}

package ass07.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class OracleActor extends UntypedActor {

    private int magicNumber;
    private int playersNumber = 0;
    private int turnPlayerMessagesReceived = 0;
    private int turnNumber = 0;
    private Map<ActorRef, Integer> playersMap; // Mantiene i riferimenti ai giocatori e le risposte di ognuno all'interno di ogni turno

    @Override
    public void preStart() {
    	this.magicNumber = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE-1);
    	this.playersMap = new HashMap<>();
        System.out.println("Oracolo: il numero sorteggiato è " + magicNumber);
    }
    
    @Override
    public void onReceive(Object message) throws Exception { // Le tipologie di messaggio sono ordinate per probabilità di arrivo
    	if (message instanceof TryNumberMessage) { // Messaggio inviato dai player contenente un numero candidato
            this.playersMap.put(getSender(), ((TryNumberMessage) message).getMessage());
            if (++this.turnPlayerMessagesReceived == this.playersNumber) { // Se in un dato turno tutti i giocatori hanno risposto
            	System.out.println("Turno " + ++this.turnNumber);
            	List<ActorRef> shuffledList = new ArrayList<>(this.playersMap.keySet());
                Collections.shuffle(shuffledList); // Per eseguire la valutazione in modo fair, mescola i player
                if (this.playersMap.containsValue(this.magicNumber)) { // Numero indovinato (comunica ai player chi ha vinto e chi ha perso)
                    boolean foundMagicNumber = false;
                    for (ActorRef player : shuffledList) {
                        if (this.playersMap.get(player) == this.magicNumber && !foundMagicNumber) {
                        	player.tell(new EndGameMessage(true), getSelf());
                            foundMagicNumber = true;
                        } else {
                        	player.tell(new EndGameMessage(false), getSelf());
                        }
                    }
                } else { // Se nessuno dei player ha indovinato nel turno, invia a tutti un suggerimento
                    shuffledList.stream().forEach(player -> 
                    		player.tell(new HintMessage(Integer.compare(this.playersMap.get(player), magicNumber)), getSelf()));
                }
                this.turnPlayerMessagesReceived = 0;  // Reinizializza il contatore per un nuovo turno
            }
        } else if (message instanceof InitializeMessage) { // Messaggio iniziale
        	((InitializeMessage) message).getMessage().stream()
        			.forEach(player -> this.playersMap.put(player, -1)); // Associa ogni player al valore -1 (non utilizzato nel resto del programma)
        	this.playersNumber = this.playersMap.size();
        	System.out.println("Turno " + ++this.turnNumber);
            this.playersMap.keySet().stream()
            		.forEach(player -> player.tell(new StartMessage(), getSelf())); // Notifica ai player di iniziare
        } else if (message instanceof EndGameMessage) {
        	if (++this.turnPlayerMessagesReceived == this.playersNumber) { // Attende che tutti i player abbiano notificato di aver terminato
        		Thread.sleep(300); // Attesa per evitare che l'oracolo termini per primo
        		context().system().shutdown();
        	}
        } else {
        	unhandled(message);
        }
    }
}

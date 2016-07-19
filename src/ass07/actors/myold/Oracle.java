package ass07.actors.myold;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

public class Oracle extends UntypedActor {

	private int magicNumber;
	private int playersNumber;
	private int turnGuessReceived = 0;
	private boolean endGame = false;
	private final List<ActorRef> playersList;
	private final Map<ActorRef, Integer> playersMap; // Mantiene le risposte di ogni utente all'interno di ogni turno
	
	public Oracle(int playersNumber) {
		this.magicNumber = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE-1);
		this.playersNumber = playersNumber;
		this.playersList = new ArrayList<>();
		this.playersMap = new HashMap<>();
	}
	
	@Override
	public void preStart() {
		for(int i=0; i<playersNumber; i++) {
			this.playersList.add(getContext().actorOf(Props.create(Player.class), "player-" + i));
		}
		for(int i=0; i<playersNumber; i++) { // Associa ogni player al valore -1 (non utilizzato nel programma)
			this.playersMap.put(this.playersList.get(i), -1);
		}
		this.playersList.parallelStream()
				.forEach(player -> player.tell("start", getSelf())); // Notifica ai player di iniziare
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(!this.endGame) { // Questa condizione diventa falsa quando uno dei giocatori ha indovinato il numero
			if(message instanceof Integer) {
				this.playersMap.put(getSelf(), (Integer)message);
				/*int evaluation = Integer.compare((int)message, this.magicNumber);
				if(evaluation==0) { // Numero indovinato (comunica ai player chi ha vinto e chi ha perso)
					System.out.println("Oracolo: numero individuato da " + getSender().path().name());
					getSender().tell("won!", getSelf());
					this.playersList.parallelStream()
							.filter(player -> player!=getSender())
							.forEach(player -> player.tell("sob!", getSelf()));
					this.endGame = true;
					context().system().shutdown();
				} else if (evaluation<0) { // Numero minore di quello reale
					getSender().tell("<", getSelf());
				} else { // Numero maggiore di quello reale
					getSender().tell(">", getSelf());
				}*/
				//this.turnGuessReceived++;
				if(++this.turnGuessReceived==this.playersNumber) { // Se tutti i giocatori hanno risposto all'interno del turno, valuta le risposte
					final List<ActorRef> shuffledList = new ArrayList<>(this.playersList); // Per eseguire la valutazione in modo fair, mescola i player
					Collections.shuffle(shuffledList);
					if(this.playersMap.containsValue(this.magicNumber)) { // Numero indovinato (comunica ai player chi ha vinto e chi ha perso)
						for(ActorRef player: shuffledList) {
							if(this.playersMap.get(player)==this.magicNumber) {
								System.out.println("Oracolo: numero individuato da " + player.path().name());
								player.tell("won!", getSelf());
							} else {
								player.tell("sob!", getSelf());
							}
						}
					} else {
						shuffledList.parallelStream()
								.forEach(player -> player.tell(Integer.compare(this.playersMap.get(player), this.magicNumber), getSelf()));
						/*for(ActorRef player: shuffledList) {
							player.tell(Integer.compare(this.playersMap.get(player), this.magicNumber), getSelf());
						}*/
					}
					this.turnGuessReceived = 0; // Reinizializza il contatore per un nuovo turno
				}
			} else {
				unhandled(message);
			}
		}
	}
}

package ass08.actors;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import pap.ass08.pos.P2d;

public class Controller extends UntypedActor {

	private ActorRef model;
	private ActorRef view;
	private int threshold, k;
	private int heartbeatValuesSum, heartbeatUpdatesCount;
	private long alarmInitialTime;
	private int maxHeartbeatValue;
	private P2d lastPosition;
	private long lastPositionTime;
	private boolean running;
	
	@Override
    public void preStart() {
		this.alarmInitialTime = System.currentTimeMillis(); // Prima inizializzazione timer
	}
	
	/* Sono presenti due sleep() per rallentare l'output che bloccano la onReceive().
	 * Effetto indesiderato, ma necessario per come è definito l'esercizio.
	 * Per evitarlo, i sensori non dovrebbero essere interrogati tramite polling, ma gestiti da thread autonomi. */
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof HeartbeatMessage) {
			if(this.running) {
				this.view.tell(new updateCurrentHeartbeatValueMessage(((HeartbeatMessage) message).getMessage().toString()), getSelf());
				this.heartbeatValuesSum += ((HeartbeatMessage) message).getMessage();
				this.heartbeatUpdatesCount++;
				this.view.tell(new updateAverageHeartbeatValueMessage(Integer.toString(this.heartbeatValuesSum/this.heartbeatUpdatesCount)), getSelf());
				if(((HeartbeatMessage) message).getMessage()>this.threshold) {
					if((System.currentTimeMillis()-this.alarmInitialTime)>this.k*1000) {
						this.view.tell(new updateAlarmNotificationMessage("True"), getSelf());
					}
				} else {
					this.alarmInitialTime = System.currentTimeMillis();
					this.view.tell(new updateAlarmNotificationMessage("False"), getSelf());
				}
				if(((HeartbeatMessage) message).getMessage()>this.maxHeartbeatValue) {
					this.maxHeartbeatValue = ((HeartbeatMessage) message).getMessage();
					if(this.lastPosition!= null) {
						this.view.tell(new updatePositionWithMaxHeartbeatValueMessage(this.lastPosition.toString()), getSelf());
					}
				}
			}
			Thread.sleep(400); 
			getSender().tell(message, getSelf());
			
		} else if(message instanceof PositionMessage) {
			if(this.running) {
				if(this.lastPosition==null) {
					this.lastPosition = ((PositionMessage) message).getMessage(); // Prima inizializzazione posizione
				}
				double distance = P2d.distance(((PositionMessage) message).getMessage(), this.lastPosition);
				this.view.tell(new updateCurrentSpeedMessage(Double.toString(distance/(System.currentTimeMillis()-this.lastPositionTime))), getSelf());
				this.lastPositionTime = System.currentTimeMillis();
				this.lastPosition = ((PositionMessage) message).getMessage();
				this.view.tell(new updateGridPositionMessage(((PositionMessage) message).getMessage()), getSelf());
			}
			Thread.sleep(400);
			getSender().tell(message, getSelf());
			
		} else if(message instanceof RunMessage) {
			if(((RunMessage) message).getMessage()==true) {
				this.running = true;
				this.model.tell(new HeartbeatMessage(0), getSelf());
				this.model.tell(new PositionMessage(null), getSelf());
			} else {
				this.running = false;
			}
		} else if(message instanceof InitializeMessage) {
			this.model = (ActorRef) ((InitializeMessage) message).getMessage().get(0);
			this.view = (ActorRef) ((InitializeMessage) message).getMessage().get(1);
			this.threshold = (int) ((InitializeMessage) message).getMessage().get(2);
			this.k = (int) ((InitializeMessage) message).getMessage().get(3);
		} else {
			unhandled(message);
		}

	}

}

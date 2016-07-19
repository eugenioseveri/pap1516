package ass08.actors;

import akka.actor.UntypedActor;
import ass08.HeartbeatSensor;
import pap.ass08.pos.PosSensor;

public class Model extends UntypedActor {

	private HeartbeatSensor hbSensor;
	private PosSensor posSensor;
	
	@Override
    public void preStart() {
		this.hbSensor = new HeartbeatSensor();
		this.posSensor = new PosSensor();
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if(message instanceof HeartbeatMessage) {
			getSender().tell(new HeartbeatMessage(this.hbSensor.getCurrentValue()), getSelf());
		} else if(message instanceof PositionMessage) {
			getSender().tell(new PositionMessage(this.posSensor.getCurrentValue()), getSelf());
		} else {
			unhandled(message);
		}
		
	}

}

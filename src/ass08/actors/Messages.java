package ass08.actors;

import java.util.List;
import akka.actor.ActorRef;
import pap.ass08.pos.P2d;

public final class Messages { }

final class InitializeMessage extends GenericMessage<List<Object>> {
	public InitializeMessage(List<Object> data) {
		super(data);
	}
}

final class RunMessage extends GenericMessage<Boolean> {
	public RunMessage(Boolean data) {
		super(data);
	}
}

/* Model Messages */

final class HeartbeatMessage extends GenericMessage<Integer> {
	public HeartbeatMessage(Integer data) {
		super(data);
	}
}

final class PositionMessage extends GenericMessage<P2d> {
	public PositionMessage(P2d data) {
		super(data);
	}
}

/* View Messages */

final class setListenerMessage extends GenericMessage<ActorRef> {
	public setListenerMessage(ActorRef data) {
		super(data);
	}
}

final class updateCurrentHeartbeatValueMessage extends GenericMessage<String> {
	public updateCurrentHeartbeatValueMessage(String data) {
		super(data);
	}
}

final class updateAverageHeartbeatValueMessage extends GenericMessage<String> {
	public updateAverageHeartbeatValueMessage(String data) {
		super(data);
	}
}

final class updateAlarmNotificationMessage extends GenericMessage<String> {
	public updateAlarmNotificationMessage(String data) {
		super(data);
	}
}

final class updateCurrentSpeedMessage extends GenericMessage<String> {
	public updateCurrentSpeedMessage(String data) {
		super(data);
	}
}

final class updatePositionWithMaxHeartbeatValueMessage extends GenericMessage<String> {
	public updatePositionWithMaxHeartbeatValueMessage(String data) {
		super(data);
	}
}

final class updateGridPositionMessage extends GenericMessage<P2d> {
	public updateGridPositionMessage(P2d data) {
		super(data);
	}
}
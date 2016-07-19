package ass07.actors;

import java.util.List;

import akka.actor.ActorRef;

public final class Messages { }

final class InitializeMessage extends GenericMessage<List<ActorRef>> {
	public InitializeMessage(List<ActorRef> data) {
		super(data);
	}
}

final class StartMessage { }

final class HintMessage extends GenericMessage<Integer> {
	public HintMessage(Integer data) {
		super(data);
	}
}

final class TryNumberMessage extends GenericMessage<Integer> {
	public TryNumberMessage(Integer data) {
		super(data);
	}
}

final class EndGameMessage extends GenericMessage<Boolean> {
	public EndGameMessage(Boolean data) {
		super(data);
	}
}

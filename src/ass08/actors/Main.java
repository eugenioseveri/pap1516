package ass08.actors;

import java.util.ArrayList;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

	public static void main(String[] args) {
		int threshold = 0, k = 0;
		try {
			threshold = Integer.parseInt(args[0]);
			k = Integer.parseInt(args[1]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.out.println("Parametri richiesti: threshold, K!");
			System.exit(1);
		} catch (NumberFormatException ex) {
			System.out.println("I parametri devono essere interi!");
			System.exit(1);
		}
		
		ActorSystem system = ActorSystem.create("TrackBeat");
		ActorRef model = system.actorOf(Props.create(Model.class), "modelActor");
		ActorRef view = system.actorOf(Props.create(View.class), "viewActor");
		ActorRef controller = system.actorOf(Props.create(Controller.class), "controllerActor");
		view.tell(new setListenerMessage(controller), ActorRef.noSender());
		
		List<Object> initParameters = new ArrayList<>();
		initParameters.add(model);
		initParameters.add(view);
		initParameters.add(threshold);
		initParameters.add(k);
		controller.tell(new InitializeMessage(initParameters), ActorRef.noSender());
	}

}

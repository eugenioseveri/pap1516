package lab07.actors.hello;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
  public static void main(String[] args) {
    ActorSystem system = ActorSystem.create("MySystem");
    ActorRef act = system.actorOf(Props.create(HappyActor.class));
    act.tell(new HelloMsg("World"), ActorRef.noSender());
  }
}

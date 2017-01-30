package actors

import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by Mikekeke on 30-Jan-17.
  */
object LiftWebSocketActor {
  def props(out: ActorRef) = Props(new LiftWebSocketActor(out))
}

class LiftWebSocketActor(out: ActorRef) extends Actor {
  def send(s: String) = out ! s

  def receive = {
    case msg: String =>
      out ! ("I received your message: " + msg)

  }
}
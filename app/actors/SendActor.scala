package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by Mikekeke on 30-Jan-17.
  */

object SendActor {
  def props(out: ActorRef) = Props(new SendActor(out))
}
class SendActor(out: ActorRef) extends Actor{
  override def receive = {
    case anything => out ! anything

  }
}

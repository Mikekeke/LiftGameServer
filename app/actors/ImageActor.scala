package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by ibes on 31.01.17.
  */
object ImageActor {
  def props(out: ActorRef) = Props(new ImageActor(out))
}

class ImageActor(out: ActorRef) extends Actor{
  override def receive: Receive = {
    case arr: String => out ! s"Got ur string"
    case arr: Array[Byte] => out ! s"Got ur image ${arr.length}"
    case other => println("other")
  }
}

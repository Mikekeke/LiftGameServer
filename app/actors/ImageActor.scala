package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}

/**
  * Created by ibes on 31.01.17.
  */
object ImageActor {
  def props(out: ActorRef) = Props(new ImageActor(out))
}

class ImageActor(out: ActorRef) extends Actor with akka.actor.ActorLogging{

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.warning("Prestarting")
    super.preStart()
  }

  override def receive: Receive = {
    case arr: String => out ! s"ImageActor: Got ur string"
    case arr: Array[Byte] => out ! s"ImageActor: Got ur image ${arr.length}"
    case other => log.warning("Received unknown message: {}", other)
  }
}

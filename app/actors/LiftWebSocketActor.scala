package actors

import akka.actor.{Actor, ActorRef, Props}
import model.QMessage
import model.api.ApiMessage

/**
  * Created by Mikekeke on 30-Jan-17.
  */
object LiftWebSocketActor {
  def props(out: ActorRef) = Props(new LiftWebSocketActor(out))
}

class LiftWebSocketActor(out: ActorRef) extends Actor with akka.actor.ActorLogging{

  def receive: PartialFunction[Any, Unit] = {
    case msg: String =>{
      log.debug("Got string")
      out ! ("LiftWebSocketActor: received your message: " + msg)
    }
    case mes: ApiMessage => {
      log.debug("Got ApiMessage")
      out ! mes.body
    }
    case other => out ! "Got something"

  }
}
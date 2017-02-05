package actors

import akka.actor.{Actor, ActorRef, PoisonPill, Props, Terminated}
import model.api.{ApiMessage, Sender}

/**
  * Created by Mikekeke on 30-Jan-17.
  */
object LiftWebSocketActor {
  def props(out: ActorRef) = Props(new LiftWebSocketActor(out))
}

class LiftWebSocketActor(out: ActorRef) extends Actor with akka.actor.ActorLogging{


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.debug("Connected")
    println("Connected")
  }


  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    log.debug("Disconnected")
    println("Disconnected")
    Sender.kill
  }

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
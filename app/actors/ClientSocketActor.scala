package actors

import akka.actor.{Actor, ActorRef, Props}
import model.api.ApiMessage
import play.api.Logger
import utils.Sender

/**
  * Created by Mikekeke on 30-Jan-17.
  */
object ClientSocketActor {
  val CLIENT_UP = "client_up"
  val CLIENT_DOWN = "client_down"
  def props(out: ActorRef) = Props(new ClientSocketActor(out))
}

class ClientSocketActor(out: ActorRef) extends Actor with akka.actor.ActorLogging{
import ClientSocketActor._

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info("Client connected")
    Sender initClient self
    Sender sendTelemetry CLIENT_UP
  }


  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    Logger.info("Client disconnected")
    Sender.onClientClosed
    Sender sendTelemetry CLIENT_DOWN
  }

  def receive: PartialFunction[Any, Unit] = {
    case msg: String =>{
      log.debug("Got string")
      out ! ("LiftWebSocketActor: received your message: " + msg)
      Sender sendTelemetry msg
    }
    case mes: ApiMessage => {
      log.debug("Got ApiMessage")
      out ! mes.body
    }
    case other => out ! "Got something"
  }
}
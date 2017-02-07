package actors

import akka.actor.{Actor, ActorRef, Props}
import model.api.{AdminMessage, ApiMessage, ClientState}
import play.api.Logger
import utils.ActorHub

/**
  * Created by Mikekeke on 30-Jan-17.
  */
object ClientSocketActor {
  val CLIENT_UP = "client_up"
  val CLIENT_DOWN = "client_down"
  def props(out: ActorRef) = Props(new ClientSocketActor(out))
}

class ClientSocketActor(out: ActorRef) extends Actor with akka.actor.ActorLogging{

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info("Client connected")
    ActorHub initClientMessaging self
    ActorHub sendToAdmin ClientState(ClientState.UP)
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    Logger.info("Client disconnected")
    ActorHub.onClientClosed
    ActorHub sendToAdmin ClientState(ClientState.DOWN)
  }

  def receive: PartialFunction[Any, Unit] = {
    case msg: String =>{
      out ! ("Server received your message: " + msg)
      Logger.warn(s"${this.getClass.getSimpleName} got string: $msg")
//      Sender sendTelemetry msg
    }
    case mes: ApiMessage => {
      out ! mes.body
    }
    case other =>
      Logger.warn(s"This came to ${this.getClass.getSimpleName}: $other")
  }
}
package actors

import akka.actor.Actor.Receive
import akka.actor.{Actor, ActorRef, Props}
import model.api.{Api, ApiMessage}
import play.api.Logger
import utils.Sender

object TelemetryActor {
  def props(out: ActorRef) = Props(new TelemetryActor(out))
}
class TelemetryActor(out: ActorRef) extends Actor{
  import ClientSocketActor._

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info("Telemetry client connected")
    Sender.initTelemetry(self)
    if (Sender.clientIsUp) self ! CLIENT_UP
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    Logger.info("Telemetry client disconnected")
  }

  override def receive: Receive = {
    case msg: String => {
      if (msg.startsWith("var_")) {
        Sender send ApiMessage(Api.Method.PICK_VARIANT, msg.substring("var_".length, msg.length))
      } else out ! msg
      Logger.info(s"Telemetry received: $msg")
    }
  }
}

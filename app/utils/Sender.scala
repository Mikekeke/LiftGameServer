package utils
import akka.actor.ActorRef
import model.api.ApiMessage
import play.api.Logger
/**
  * Created by ibes on 06.02.17.
  */
object Sender {

  private var senderRef: Option[ActorRef] = None
  private var telemetryRef: Option[ActorRef] = None
  def initClient(ref: ActorRef): Unit = senderRef = Some(ref)
  def onClientClosed : Unit = senderRef = None
  def clientIsUp = senderRef.nonEmpty

  def send(apiMessage: ApiMessage): Unit =
    if (senderRef.nonEmpty) senderRef.get ! apiMessage
    else {
      Logger.warn("no client connected")
    }

  def send(msg: String): Unit =
    if (senderRef.nonEmpty) senderRef.get ! msg else Logger.warn("no client connected")

  def initTelemetry(ref: ActorRef): Unit = telemetryRef = Some(ref)
  def sendTelemetry(apiMessage: String): Unit =
    if (telemetryRef.nonEmpty) telemetryRef.get ! apiMessage
    else {
      Logger.warn("Telemetry down")
    }

}

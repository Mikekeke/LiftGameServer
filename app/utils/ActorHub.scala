package utils

import akka.actor.ActorRef
import model.api._
import play.api.Logger

/**
  * Created by ibes on 06.02.17.
  */
object ActorHub {



  private var clientsRef: Option[ActorRef] = None
  private var adminsRef: Option[ActorRef] = None

  def initClientMessaging(ref: ActorRef): Unit = clientsRef = Some(ref)

  def initAdminMessaging(ref: ActorRef): Unit = adminsRef = Some(ref)

  def onClientClosed: Unit = clientsRef = None

  def clientIsUp: Boolean = clientsRef.nonEmpty

  def sendToClient(apiMessage: ApiMessage): Unit =
    if (clientsRef.nonEmpty) clientsRef.get ! apiMessage else Logger.warn("ApiMessage: No client connected")

  def sendToClient(msg: String): Unit =
    if (clientsRef.nonEmpty) clientsRef.get ! msg else Logger.warn("String: No client connected")

  def sendTelemetry(tel: Telemetry): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! tel else Logger.warn("Telemetry: No admin connected")

  def sendToAdmin(clientState: ClientState): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! clientState else Logger.warn("ClientState: No admin connected")

  def sendCheck(check: Check): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! check else Logger.warn("Check: No admin connected")

  def sendLogo(logo: Logo): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! logo else Logger.warn("Logo: No admin connected")

  def sendTQuestion(tQuestion: TQuestion): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! tQuestion else Logger.warn("TQuestion: No admin connected")

  def sendTimer(timer: Timer): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! timer else Logger.warn("Timer: No admin connected")

  def sendAnswer(answer: Answer): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! answer else Logger.warn("Answer: No admin connected")

}

package utils
import akka.actor.ActorRef
import model.api.{ApiMessage, ClientState}
import play.api.Logger
/**
  * Created by ibes on 06.02.17.
  */
object ActorHub {

  private var clientsRef: Option[ActorRef] = None
  private var adminsRef: Option[ActorRef] = None
  def initClientMessaging(ref: ActorRef): Unit = clientsRef = Some(ref)
  def initAdminMessaging(ref: ActorRef): Unit = adminsRef = Some(ref)

    def onClientClosed : Unit = clientsRef = None

    def clientIsUp: Boolean = clientsRef.nonEmpty

    def sendToClient(apiMessage: ApiMessage): Unit =
    if (clientsRef.nonEmpty) clientsRef.get ! apiMessage else Logger.warn("no client connected")

    def sendToClient(msg: String): Unit =
    if (clientsRef.nonEmpty) clientsRef.get ! msg else Logger.warn("no client connected")

    def sendToAdmin(apiMessage: String): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! apiMessage else Logger.warn("No admin connected")

  def sendToAdmin(clientState: ClientState): Unit =
    if (adminsRef.nonEmpty) adminsRef.get ! clientState else Logger.warn("No admin connected")
}

package actors

import akka.actor.{Actor, ActorRef, Props}
import model.api.{AdminMessage, Api, ApiMessage, ClientState}
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import utils.ActorHub

import scala.util.{Failure, Success, Try}

object AdminSocketActor {
  def props(out: ActorRef) = Props(new AdminSocketActor(out))
}

class AdminSocketActor(out: ActorRef) extends Actor {

  import ClientSocketActor._

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info("Telemetry client connected")
    ActorHub.initAdminMessaging(self)
    if (ActorHub.clientIsUp) self ! ClientState(ClientState.UP)
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    Logger.info("Telemetry client disconnected")
  }

  override def receive: Receive = {
    case state: ClientState => out ! state.toJson
    case jsObject: JsObject =>
      Try(AdminMessage.fromJson(jsObject)) match {
        case Success(message) =>
          ActorHub sendToClient ApiMessage(message.action, message.obj)
        case Failure(e) =>
          Logger.error("Parsing error", e)
      }
      Logger.info(s"Message from Admin: $jsObject")

    case other =>
      Logger.warn(s"This came to ${this.getClass.getSimpleName}: $other")
  }
}

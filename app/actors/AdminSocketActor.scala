package actors

import akka.actor.{Actor, ActorRef, Props}
import model.api._
import play.api.Logger
import play.api.libs.json.{JsObject, Json}
import utils.ActorHub

import scala.util.{Failure, Success, Try}

object AdminSocketActor {
  def props(out: ActorRef) = Props(new AdminSocketActor(out))
}

class AdminSocketActor(out: ActorRef) extends Actor {


  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    Logger.info("Admin connected")
    ActorHub.initAdminMessaging(self)
    if (ActorHub.clientIsUp) self ! ClientState(ClientState.UP)
    else self ! ClientState(ClientState.DOWN)
  }

  @scala.throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    Logger.info("Admin disconnected")
  }

  override def receive: Receive = {
    case tQuestion: TQuestion => out ! tQuestion.toJson
    case logo: Logo => out ! logo.toJson
    case check: Check => out ! check.toJson
    case state: ClientState => out ! state.toJson
    case tel: Telemetry => out ! tel.toJson
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

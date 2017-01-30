package controllers

import javax.inject._

import actors.{LiftWebSocketActor}
import akka.actor.{ActorSystem}
import akka.stream.Materializer
import model.{Question, TempHolder}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.streams._

/**
  * Created by Mikekeke on 30-Jan-17.
  */
@Singleton
class WSController @Inject()
(implicit system: ActorSystem, materializer: Materializer)
  extends Controller {
  var r: LiftWebSocketActor = null

  val socket1: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => LiftWebSocketActor.props(out))
  }

  def socket = socket1

  def send(s: String) = Action {implicit request =>
    Redirect(routes.WSController.index())
  }

  val jsons = TempHolder.questions.map(Json.toJson[Question]).map(_.toString())
  def index = Action {implicit request =>
    Ok(views.html.index(jsons))
  }
}

//val socket1: WebSocket = WebSocket.accept[String, String] { request =>
//  ActorFlow.actorRef(out => {
//  val props = LiftWebSocketActor.props(out)
//  val socketActor = system.actorOf(props)
//  sender = system.actorOf(SendActor.props(socketActor))
//  props
//})
//}
//
//  def socket = socket1
//
//  def send(s: String) = Action {implicit request =>
//  sender ! "Test shit"
//
//  Redirect(routes.WSController.index())
//}
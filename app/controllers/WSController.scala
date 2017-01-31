package controllers

import javax.inject._

import actors.{LiftWebSocketActor, SendActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import model.api.{Api, ApiMessage}
import model.{QMessage, Question, TempHolder}
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

//  val socket1: WebSocket = WebSocket.accept[String, String] { request =>
//    ActorFlow.actorRef(out => LiftWebSocketActor.props(out))
//  }
//
//  def socket = socket1
//
//  def send(s: String) = Action {implicit request =>
//    Redirect(routes.WSController.index())
//  }

  val jsons: List[String] = TempHolder.questions.map(Json.toJson[Question]).map(_.toString())
  def index = Action {implicit request =>
    Ok(views.html.index(jsons))
  }

  var sender: ActorRef = null
  val socket1: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef(out => {
      val props = LiftWebSocketActor.props(out)
      val socketActor = system.actorOf(props)
      sender = system.actorOf(SendActor.props(socketActor))
      props
    })
  }

  def socket = socket1

  def send(s: String) = Action {implicit request =>
    sender ! ApiMessage(Api.Method.QUESTION, s)
    Redirect(routes.WSController.index())
  }

  def pickQuestion = Action { implicit request =>
    sender ! ApiMessage(Api.Method.LOGO, "")
    Redirect(routes.WSController.index())
  }


  def check() = Action {implicit request =>
    sender ! ApiMessage(Api.Method.CHECK, "")
    Redirect(routes.WSController.index())
  }
}


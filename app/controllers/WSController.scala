package controllers

import java.util.concurrent.TimeUnit
import javax.inject._

import actors.{ImageActor, LiftWebSocketActor, SendActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import model.api.{Api, ApiMessage}
import model.{QMessage, Question, TempHolder}
import play.api.libs.json.Json
import play.api.mvc._
import play.api.libs.streams._

import scala.concurrent.duration.Duration

/**
  * Created by Mikekeke on 30-Jan-17.
  */
@Singleton
class WSController @Inject()
(implicit system: ActorSystem, materializer: Materializer)
  extends Controller {

//  val socket1: WebSocket = WebSocket.accept[String, String] { request =>
//    ActorFlow.actorRef(out => LiftWebSocketActor.props(out))
//  }
//
//  def socket = socket1
//
//  def send(s: String) = Action {implicit request =>
//    Redirect(routes.WSController.index())
//  }


  val s = " dfd"
  def index = Action {implicit request =>
    Ok(views.html.index(currentQuestion.getOrElse("NOT SET")))
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

  val jsons: List[String] = TempHolder.questions.map(Json.toJson[Question]).map(_.toString())
  def pickQuestion = Action { implicit request =>
    sender ! ApiMessage(Api.Method.LOGO, "")
    Ok(views.html.questions(jsons))
  }

  var currentQuestion: Option [String] = None
  def setQuestion(s: String) = Action { implicit request =>
    currentQuestion = Some(s)
    Redirect(routes.WSController.index())
  }

  def pickVariant() = Action {implicit request =>
    request.body.asFormUrlEncoded.get("send_variant").headOption match {
      case Some("var_1") => sender ! ApiMessage(Api.Method.PICK_VARIANT, "1")
      case Some("var_2") => sender ! ApiMessage(Api.Method.PICK_VARIANT, "2")
      case Some("var_3") => sender ! ApiMessage(Api.Method.PICK_VARIANT, "3")
      case Some("var_4") => sender ! ApiMessage(Api.Method.PICK_VARIANT, "4")
    }

    Redirect(routes.WSController.index())
  }

  def questionAction = Action {implicit request =>
    request.body.asFormUrlEncoded.get("qaction").headOption match {
      case Some("check") => sender ! ApiMessage(Api.Method.CHECK, "")
      case Some("expand") => sender ! ApiMessage(Api.Method.EXPAND_ANSWER, "")
    }
    Redirect(routes.WSController.index())
  }

  import system.dispatcher
  def testScheduler = Action {implicit  request =>
    sender ! ApiMessage("screenshot", "")
//    system.scheduler.schedule(Duration(0, TimeUnit.SECONDS), Duration(2, TimeUnit.SECONDS)) {
//      sender ! "give image"
//    }
    Redirect(routes.WSController.index())
  }

  def imgsocket: WebSocket = WebSocket.accept[String, String] {request =>
    ActorFlow.actorRef(out => ImageActor.props(out))
  }
}


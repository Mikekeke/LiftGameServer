package controllers

import javax.inject._

import actors.{ClientSocketActor, ImageActor, TelemetryActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import model.Question
import model.api.{Api, ApiMessage}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams._
import play.api.mvc._
import utils.{ActorHub, CachedQuestion, ExcelParser}

import scala.util.{Success, Try}

/**
  * Created by Mikekeke on 30-Jan-17.
  */
@Singleton
class WSController @Inject()
(implicit system: ActorSystem, materializer: Materializer)
  extends Controller {

  def index = Action { implicit request =>
    val url = routes.WSController.adminSocket().webSocketURL()
    CachedQuestion.get match {
      case Some(q) =>
        Ok(views.html.index(q.toDescription)(url))
      case _ => Ok(views.html.index(Question.NOT_SET)(url))
    }
  }

  def clientSocket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef((out: ActorRef) => ClientSocketActor.props(out))
  }

  def adminSocket: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef((out: ActorRef) => TelemetryActor.props(out))
  }

  def send = Action { implicit request =>
    ActorHub send ApiMessage(Api.Method.QUESTION, Json.toJson(CachedQuestion.get.get).toString())
    Redirect(routes.WSController.index())
  }

  def pickQuestion = Action { implicit request =>
    // TODO: NULL check or how to handle this??
    ActorHub send ApiMessage(Api.Method.LOGO)

    Redirect(routes.XLController.indexQuestions())

  }

  def pickVariant() = Action { implicit request =>
    request.body.asFormUrlEncoded.get("send_variant").headOption match {
      case Some("var_1") => ActorHub send ApiMessage(Api.Method.PICK_VARIANT, "1")
      case Some("var_2") => ActorHub send ApiMessage(Api.Method.PICK_VARIANT, "2")
      case Some("var_3") => ActorHub send ApiMessage(Api.Method.PICK_VARIANT, "3")
      case Some("var_4") => ActorHub send ApiMessage(Api.Method.PICK_VARIANT, "4")
    }
    Redirect(routes.WSController.index())
  }

  def questionAction = Action { implicit request =>
    request.body.asFormUrlEncoded.get("qaction").headOption match {
      case Some("check") => ActorHub send ApiMessage(Api.Method.CHECK)
      case Some("expand") => ActorHub send ApiMessage(Api.Method.EXPAND_ANSWER)
      case Some("done") => makeQuestionAsked(CachedQuestion.getNum)
    }
    Redirect(routes.WSController.index())
  }

  def makeQuestionAsked(qNum: Int) = {
    Try(ExcelParser) match {
      case Success(reader) => {
        reader.makeAskedQuestionNumber(qNum)
        CachedQuestion.clear
      }
      // TODO: log error
      case _ => {
        println("logerror")
      }
    }
  }

  def testScheduler = Action { implicit request =>
    //    sender ! ApiMessage("screenshot", "")
    //    system.scheduler.schedule(Duration(0, TimeUnit.SECONDS), Duration(2, TimeUnit.SECONDS)) {
    //      sender ! "give image"
    //    }
    ActorHub sendTelemetry "give image"
    Redirect(routes.WSController.index())
  }

  def imgsocket: WebSocket = WebSocket.accept[Array[Byte], Array[Byte]] { request =>
    ActorFlow.actorRef(out => ImageActor.props(out))
  }
}


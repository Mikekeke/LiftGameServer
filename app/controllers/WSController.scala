package controllers

import java.io.File
import javax.inject._

import actors.{AdminSocketActor, ClientSocketActor, ImageActor}
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import model.Question
import model.api.{Api, ApiMessage}
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.libs.streams._
import play.api.mvc._
import utils.{ActorHub, ExcelParser, FileUtils, QuestionCache}

import scala.util.{Failure, Success, Try}

/**
  * Created by Mikekeke on 30-Jan-17.
  */
@Singleton
class WSController @Inject()
(implicit system: ActorSystem, materializer: Materializer, excelParser: ExcelParser, fileUtils: FileUtils)
  extends Controller {

  def index = Action { implicit request =>
    val url = routes.WSController.adminSocket().webSocketURL()
    val qForPreview: Seq[String] = QuestionCache.getAll.getOrElse(Seq.empty).map(_.name)
    QuestionCache.getCurrent match {
      case Some(q) =>
        Ok(views.html.index(q.toDescription)(url)(qForPreview)(q))
      case _ =>
        Ok(views.html.index(Question.NOT_SET)(url)(Seq.empty)(null))
    }
  }

  def clientSocket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef((out: ActorRef) => ClientSocketActor.props(out))
  }

  def adminSocket: WebSocket = WebSocket.accept[JsValue, JsValue] { request =>
    ActorFlow.actorRef((out: ActorRef) => AdminSocketActor.props(out))
  }

  def send = Action { implicit request =>
    if (QuestionCache.getCurrent.nonEmpty) {
      ActorHub sendToClient ApiMessage(Api.Method.QUESTION, QuestionCache.getCurrent.get.toJson.toString())
    }
    Redirect(routes.WSController.index())
  }

  def pickQuestion = Action { implicit request =>
    // TODO: NULL check or how to handle this??
    ActorHub sendToClient ApiMessage(Api.Method.LOGO)

    Redirect(routes.XLController.indexQuestions())

  }

  def questionDone = Action { implicit request =>
    makeQuestionAsked(QuestionCache.getNum)
    Redirect(routes.WSController.index())
  }

  def makeQuestionAsked(qNum: Int) = {
    ActorHub sendToClient ApiMessage(Api.Method.LOGO)
    Try(excelParser.makeAskedQuestionNumber(qNum)) match {
      case Success(any) =>
        Logger.info(s"Question -${QuestionCache.getCurrent.get.name}- with number = $qNum now ASKED")
        QuestionCache.clear
      case Failure(e) =>
        Logger.error("Error on making question asked", e)
    }
  }

  def questionScheme(qName: String) = Action { implicit request =>
    if (QuestionCache.getAll.nonEmpty) {
      val question: Option[Question] = QuestionCache.getAll.get.find(_.name == qName)
      if (question.nonEmpty) Ok(views.html.qview(question.get))
      else BadRequest
    }
    else BadRequest
  }

  def testScheduler = Action { implicit request =>
    //    sender ! ApiMessage("screenshot", "")
    //    system.scheduler.schedule(Duration(0, TimeUnit.SECONDS), Duration(2, TimeUnit.SECONDS)) {
    //      sender ! "give image"
    //    }
    //    ActorHub sendToAdmin "give image"
    Redirect(routes.WSController.index())
  }

  def getImageByName(imgName: String) = Action { implicit request =>
    try {
      val dir = new File(fileUtils.getImagesDir.get)
      val files = dir.list()
      if (files.nonEmpty && files.contains(imgName)) Ok.sendFile(new File(dir, imgName))
      else NotFound(s"Can't get file: $imgName")

    } catch {
      case e: Exception =>
        Logger.error("Error getting image: ", e)
        BadRequest(s"Can't get file: $imgName: ${e.getMessage}")
    }
  }

  def imgsocket: WebSocket = WebSocket.accept[Array[Byte], Array[Byte]] { request =>
    ActorFlow.actorRef(out => ImageActor.props(out))
  }
}


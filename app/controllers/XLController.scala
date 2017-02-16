package controllers

import java.io.{File, FileNotFoundException}
import javax.inject._
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

import akka.actor.ActorSystem
import akka.stream.Materializer
import froms.QForm
import model.Question
import play.api.{Configuration, Logger, Play}
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import utils.{ExcelParser, FileUtils, QuestionCache}

import scala.util.{Failure, Success, Try}

/**
  * Created by ibes on 02.02.17.
  */
@Singleton
class XLController @Inject()
(implicit system: ActorSystem, materializer: Materializer,  configuration: Configuration,
 fileUtils: FileUtils, excelParser: ExcelParser)
  extends Controller {

  //  var file: Option[File] = None
  val excelExtFilter = new FileNameExtensionFilter("Excel", "xlsx")

//  var QUESTIONS: Seq[Question] = Seq.empty

  def indexQuestions = Action { implicit request =>
    Try(excelParser.getQuestions) match {
      case Success(questions) =>
        val filteredQuestions = questions.filterNot(_.status == Question.Status.ASKED)
        QuestionCache.cacheAll(filteredQuestions)
        Ok(views.html.excel_questions(excelParser.path, filteredQuestions, questionForm))
      case Failure(e) =>
        Ok(views.html.excel_questions(e.getMessage, Seq.empty, questionForm))
    }
  }

  def downloadExcel = Action {
    fileUtils.getExcelFilePath match {
      case Success(path) =>
        val file = new File(path)
        Try(Ok.sendFile(file)).getOrElse(BadRequest("Файл не найден"))
      case Failure(e) =>
        Logger.error("Error downloading file", e)
        BadRequest("Ошибка при скачивании файла")
    }
  }

  def uploadExcelFile = Action(parse.multipartFormData) { request =>
    request.body.file("excel").map { excel =>
      import java.io.File
      val filename = excel.filename
      if (filename.nonEmpty) {
        val contentType = excel.contentType
        configuration.getString("uploadDir") match {
          case Some(path) =>
            val file = new File(path, filename)
            if (file.exists()) file.delete()
            val result = excel.ref.moveTo(file)
            fileUtils.persistExcelFilePath(result.getAbsolutePath)
        }
      }
      Redirect(routes.XLController.indexQuestions())
    }.getOrElse {
      Redirect(routes.XLController.indexQuestions()).flashing(
        "error" -> "Missing file")
    }
  }

  def uploadImages = Action(parse.multipartFormData) { request =>
    try {
      val files = request.body.files
      val imgDir = new File(fileUtils.getImagesDir.get)
      if (!imgDir.exists()) imgDir.mkdir()
      files.foreach(file => {
        val toSave = new File(imgDir, file.filename)
        if (toSave.exists()) toSave.delete()
        file.ref.moveTo(toSave)
      })
      Redirect(routes.XLController.indexQuestions())
    } catch {
      case e: Exception => BadRequest("Ошбка при сохранении изображений: " + e.getMessage)
    }
  }

  import play.api.data.Forms._
  import play.api.data._

  val questionForm: Form[QForm] = Form(
    tuple(
      "number" -> number,
      "name" -> text,
      "question" -> text,
      "v1" -> text,
      "v2" -> text,
      "v3" -> text,
      "v4" -> text,
      "v_corr" -> number,
      "answer" -> text,
      "status" -> text
    )
  )

  def saveOrPick(questionNum: Int) = Action { implicit request =>
    val form = questionForm.bindFromRequest
    if (form.hasErrors) {
      println(form.errors)
      Redirect(routes.XLController.indexQuestions())
    } else {
      val q = Question fromData form.data
      request.body.asFormUrlEncoded.get("proc_q").headOption match {
        case Some("save") => {
          Try(excelParser.saveQuestion(questionNum, q)) match {
            case Success(_) => {
              Redirect(routes.XLController.indexQuestions())
            }
            // TODO: log error
            case _ => {
              println("logerror")
              Redirect(routes.XLController.indexQuestions())
            }
          }
        }
        case Some("pick") => {
          QuestionCache.cacheCurrent(questionNum, Question fromData form.data)
          Redirect(routes.WSController.index())
        }
      }
    }
  }
}

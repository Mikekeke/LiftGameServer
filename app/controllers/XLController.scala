package controllers

import java.io.File
import javax.inject._
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

import akka.actor.ActorSystem
import akka.stream.Materializer
import froms.QForm
import model.Question
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.mvc._
import utils.{CachedQuestion, ExcelParser, FileUtils}

import scala.util.{Failure, Success, Try}

/**
  * Created by ibes on 02.02.17.
  */
@Singleton
class XLController @Inject()
(implicit system: ActorSystem, materializer: Materializer)
  extends Controller {

  //  var file: Option[File] = None
  val excelExtFilter = new FileNameExtensionFilter("Excel", "xlsx")

//  var QUESTIONS: Seq[Question] = Seq.empty

  def indexQuestions = Action { implicit request =>
    Try(ExcelParser.getQuestions) match {
      case Success(questions) => {
        Ok(views.html.excel_questions(ExcelParser.path, questions, questionForm))
      }
      case Failure(e) =>
        Ok(views.html.excel_questions(e.getMessage, Seq.empty, questionForm))
    }
  }

  def openFile = Action { request =>
    val file = openExcel
    if (file.nonEmpty) FileUtils.persistExcelFilePath(file.get.getAbsolutePath)
    Redirect(routes.XLController.indexQuestions())
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
          Try(ExcelParser.saveQuestion(questionNum, q)) match {
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
          CachedQuestion.cache(questionNum, Question fromData form.data)
          Redirect(routes.WSController.index())
        }
      }
    }
  }

  def openExcel: Option[File] = {
    val chooser = new JFileChooser()
    chooser.setDialogTitle("Выберите файл с вопросами")
    chooser.setFileFilter(excelExtFilter)
    chooser.showOpenDialog(null) match {
      case JFileChooser.APPROVE_OPTION =>
        Some(chooser.getSelectedFile)
      case _ => None
    }
  }
}

package controllers

import java.io.File
import java.lang.Exception
import javafx.stage.FileChooser
import javax.inject._
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

import akka.actor.ActorSystem
import akka.stream.Materializer
import froms.QForm
import model.Question
import play.api.data.Form
import play.api.mvc._
import utils.{ExcelUtil, FileUtils}

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


  var QUESTIONS: Seq[Question] = Seq.empty

  def indexQuestions = Action { implicit request =>
    FileUtils.getExcelFilePath match {
      case Success(path) =>
        Try(ExcelUtil.ExcelReader(path)) match {
          case Success(reader) => {
            QUESTIONS = reader.getQuestions
            Ok(views.html.excel_questions(path, QUESTIONS, questionForm))
          }
          case Failure(e) =>
            Ok(views.html.excel_questions(e.getMessage, QUESTIONS, questionForm))
        }
      case Failure(t) =>
        Ok(views.html.excel_questions(t.getMessage, QUESTIONS, questionForm))
    }
  }

  def openFile = Action { request =>
    val file = openExcel
    if (file.nonEmpty) FileUtils.persistExcelFilePath(file.get.getAbsolutePath)
    Redirect(routes.XLController.indexQuestions())
  }

  import play.api.data._
  import play.api.data.Forms._

  val questionForm: Form[QForm] = Form(
    tuple(
      "num" -> number,
      "name"-> text,
      "question" -> text,
      "v1" -> text,
      "v2" -> text,
      "v3" -> text,
      "v4" -> text,
      "v_corr" -> number,
      "answer" -> text,
      "status" -> number
    )
  )
case class UserData(name: String, age: Int)
  val userForm = Form(
    mapping(
      "name" -> text,
      "age" -> number
    )(UserData.apply)(UserData.unapply)
  )


  def save(questionNum: Int) = Action { request =>
    val re = request.body.asFormUrlEncoded.get("q_property").map(_.toString)
    FileUtils.getExcelFilePath match {
      case Success(path) => {
        Try(ExcelUtil.ExcelReader(path)) match {
//          case Success(reader) => reader.saveQuestion(questionNum, QUESTIONS(questionNum))
          case Success(reader) => println("save")
          // TODO: log error
          case _ => println("logerror")
        }
      }
      case Failure(t) => t.printStackTrace()
    }

    Redirect(routes.XLController.indexQuestions())
  }

  def testFile = Action { request =>


    //      if (path.nonEmpty) ExcelUtil.ExcelReader(path.get)
    Redirect(routes.XLController.indexQuestions())
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

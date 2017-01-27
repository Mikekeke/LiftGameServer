package controllers

import model.{Question, TempHolder}
import play.api.libs.json.{Format, Json}
import play.api.mvc._

object Application extends Controller {
  def index = Action {
    val json = TempHolder.questions.map(Json.toJson[Question]).mkString
    Ok(views.html.index(json))
  }

}
package controllers

import model.{Question, TempHolder}
import play.api.libs.json.{Format, Json}
import play.api.mvc._

object Application extends Controller {
  def index = Action {
    val jsons = TempHolder.questions.map(Json.toJson[Question]).map(_.toString())
    Ok(views.html.index(jsons))
  }

}
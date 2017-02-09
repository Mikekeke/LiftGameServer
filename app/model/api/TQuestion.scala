package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 09-Feb-17.
  */
case class TQuestion(question: String) {
  def toJson = Json.toJson(this)
}

object TQuestion {
  implicit val questionFormat: Format[TQuestion] = Json.format[TQuestion]
}
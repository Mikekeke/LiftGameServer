package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 09-Feb-17.
  */
case class Answer(answer: String) {
  def toJson = Json.toJson(this)
}

object Answer {
  implicit val questionFormat: Format[Answer] = Json.format[Answer]
}
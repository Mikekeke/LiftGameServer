package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 09-Feb-17.
  */
case class Timer(timer: String) {
  def toJson = Json.toJson(this)
}

object Timer {
  implicit val questionFormat: Format[Timer] = Json.format[Timer]
}
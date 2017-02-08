package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by ibes on 08.02.17.
  */
case class Check(check: String) {
  def toJson = Json.toJson(this)
}

object Check {
  implicit val questionFormat: Format[Check] = Json.format[Check]
}
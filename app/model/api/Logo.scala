package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 09-Feb-17.
  */
case class Logo(logo: String) {
  def toJson = Json.toJson(this)
}

object Logo {
  implicit val questionFormat: Format[Logo] = Json.format[Logo]
}
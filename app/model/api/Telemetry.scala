package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by ibes on 08.02.17.
  */
case class Telemetry(variant: String) {
  def toJson = Json.toJson(this)
}

object Telemetry {
  implicit val questionFormat: Format[Telemetry] = Json.format[Telemetry]
}
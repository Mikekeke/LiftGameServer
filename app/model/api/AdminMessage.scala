package model.api

import play.api.libs.json.{Format, JsObject, Json}


case class AdminMessage(action: String, obj: String) {
  def this(action: String) = this(action, "")
  def toJson = Json.toJson(this)
}

object AdminMessage {
  def apply(action: String) = new ApiMessage(action)
  def fromJson(obj: JsObject): AdminMessage = obj.as[AdminMessage]
  implicit val adminMessageFormat: Format[AdminMessage] = Json.format[AdminMessage]
}

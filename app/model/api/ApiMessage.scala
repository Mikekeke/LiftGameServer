package model.api

import model.QStatus
import play.api.libs.json.{Format, Json}

/**
  * Created by ibes on 31.01.17.
  */
case class ApiMessage(method: String, content: String) {
  def body: String = Json.toJson(this).toString()
}

object ApiMessage {
  implicit val variantFormat: Format[ApiMessage] = Json.format[ApiMessage]
}
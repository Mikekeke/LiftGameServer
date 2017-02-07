package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by ibes on 31.01.17.
  */
case class ApiMessage(method: String, content: String) {
  def this(method: String) = this(method, "")
  def body: String = Json.toJson(this).toString()
}

object ApiMessage {
  def apply(method: String) = new ApiMessage(method)
  implicit val variantFormat: Format[ApiMessage] = Json.format[ApiMessage]
}
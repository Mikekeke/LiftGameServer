package model.api

import play.api.libs.json.{Format, Json}

/**
  * Created by ibes on 07.02.17.
  */
case class ClientState(state: String) {
  def toJson = Json.toJson(this)
}

object ClientState {
  val UP = "client_up"
  val DOWN = "client_down"
  implicit val questionFormat: Format[ClientState] = Json.format[ClientState]
}
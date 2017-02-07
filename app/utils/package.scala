import play.api.libs.json.{Format, Json}

/**
  * Created by ibes on 02.02.17.
  */
package object froms {
  type QForm = (Int, String, String, String, String, String, String, Int, String, String)

}

package object messages {

  case class ClientState(state: String)

  object ClientState {
    val UP = "client_up"
    val DOWN = "client_down"
    implicit val questionFormat: Format[ClientState] = Json.format[ClientState]
  }

}

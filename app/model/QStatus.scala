package model

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 27-Jan-17.
  */
case class QStatus(value: Int) {
}

object QStatus {
  val NDF = QStatus(-1)

  implicit val variantFormat: Format[QStatus] = Json.format[QStatus]
}

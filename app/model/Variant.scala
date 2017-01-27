package model

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 27-Jan-17.
  */
case class Variant(num: Int, text: String) {
  require(num > 0 && num <= 4, "Invalid variant number")
  require(text.nonEmpty, "Empty text")
}
object Variant {
  val FIRST = 1
  val SECOND = 2
  val THIRD = 3
  val FOURTH = 4

  implicit val variantFormat: Format[Variant] = Json.format[Variant]
}
package model

import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 27-Jan-17.
  */
case class Question(num: Int, name:String, question: String,
                    variants: Map[String, Variant], correctVar: Short, answer: String,
                    status: QStatus, ing1: String, img2: String) {
  require(variants.size == 4, s"Should be 4 variants, now ${variants.size}")
}

object Question {
  implicit val questionFormat: Format[Question] = Json.format[Question]
}

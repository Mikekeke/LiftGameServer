package model

import org.apache.poi.ss.usermodel.Row
import play.api.libs.json.{Format, Json}
import play.mvc.BodyParser.AnyContent

/**
  * Created by Mikekeke on 27-Jan-17.
  */
case class Question(number: Int, name:String, question: String, correctVar: Int, answer: String,
                    status: String = Question.Status.NOT_ASKED,
                    img1: String = "", img2: String = "",
                    variants: Map[String, String]) {
  require(variants.size == 4, s"Should be 4 variants, now ${variants.size}")


//  def toExcelString: String = s"$num|$name|$question" +
//    s"|${variants("1")}|${variants("2")}|${variants("3")}|${variants("4")}" +
//    s"|$answer|${status.value}|$img1|$img2"

  def toJson = Json.toJson(this)
  def toJSString = toJson.toString()
  def toDescription = s"№ $number - $name: $question &&" +
    s"Ответ: $answer&&" +
    s"Правильный вариант: №$correctVar - ${variants(correctVar.toString)}"

  def toRow(row: Row) = {
    row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellValue(number)
    row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellValue(name)
    row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellValue(question)
    row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("1"))
    row.getCell(4, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("2"))
    row.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("3"))
    row.getCell(6, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("4"))
    row.getCell(7, Row.CREATE_NULL_AS_BLANK).setCellValue(correctVar)
    row.getCell(8, Row.CREATE_NULL_AS_BLANK).setCellValue(answer)
    row.getCell(9, Row.CREATE_NULL_AS_BLANK).setCellValue(status)
    row.getCell(10, Row.CREATE_NULL_AS_BLANK).setCellValue(img1)
    row.getCell(11, Row.CREATE_NULL_AS_BLANK).setCellValue(img2)
  }
}

object Question {
  val NOT_SET = "Не выбран"
  object Status{
    val ASKED = "задан"
    val NOT_ASKED = "не задан"
  }
  implicit val questionFormat: Format[Question] = Json.format[Question]
  def fromData(data: Map[String, String]): Question = Question(
    data("number").toInt,
    data("name"),
    data("question"),
    data("v_corr").toInt,
    data("answer"),
    data("status"),
    "",
    "",
    Map(
      "1" -> data("v1"),
      "2" -> data("v2"),
      "3" -> data("v3"),
      "4" -> data("v4")
    )
  )
}

package model

import org.apache.poi.ss.usermodel.Row
import play.api.libs.json.{Format, Json}

/**
  * Created by Mikekeke on 27-Jan-17.
  */
case class Question(num: Int, name:String, question: String,
                    variants: Map[String, Variant], correctVar: Short, answer: String,
                    status: QStatus, img1: String = "", img2: String = "") {
  require(variants.size == 4, s"Should be 4 variants, now ${variants.size}")

//  def toExcelString: String = s"$num|$name|$question" +
//    s"|${variants("1")}|${variants("2")}|${variants("3")}|${variants("4")}" +
//    s"|$answer|${status.value}|$img1|$img2"

  def toRow(row: Row) = {
    row.getCell(0, Row.CREATE_NULL_AS_BLANK).setCellValue(num)
    row.getCell(1, Row.CREATE_NULL_AS_BLANK).setCellValue(name)
    row.getCell(2, Row.CREATE_NULL_AS_BLANK).setCellValue(question)
    row.getCell(3, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("1").text)
    row.getCell(4, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("2").text)
    row.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("3").text)
    row.getCell(6, Row.CREATE_NULL_AS_BLANK).setCellValue(variants("4").text)
    row.getCell(7, Row.CREATE_NULL_AS_BLANK).setCellValue(correctVar)
    row.getCell(8, Row.CREATE_NULL_AS_BLANK).setCellValue(correctVar)
    row.getCell(9, Row.CREATE_NULL_AS_BLANK).setCellValue(status.value)
    row.getCell(10, Row.CREATE_NULL_AS_BLANK).setCellValue(img1)
    row.getCell(11, Row.CREATE_NULL_AS_BLANK).setCellValue(img2)
  }
}

object Question {
  implicit val questionFormat: Format[Question] = Json.format[Question]
}

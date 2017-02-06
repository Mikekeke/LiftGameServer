package utils

import java.io.{File, FileInputStream, FileOutputStream}

import model.Question
import org.apache.poi.ss.usermodel.{Row, Sheet, Workbook, WorkbookFactory}

import scala.collection.immutable.Seq
import scala.util.{Failure, Success, Try}

object ExcelParser {
  private val cellsQuantity: Int = 12 - 1
  private val statusCell = 9
  // TODO: push Fileobject here


  private var in: FileInputStream = _
  private var wb: Workbook = _
  private var sheet: Sheet = _
  private var rowsNum: Int = _
  private var rows: Seq[Row] = _
  private var filePath: String = _

  private def init(): Unit = {
    if (in == null || wb == null || filePath == null) {
      FileUtils.getExcelFilePath match {
        case Success(path) =>
          filePath = path.ensuring(path.nonEmpty, "File path is empty")
          require(new File(filePath).exists(), s"File at path $filePath not found")
          in = new FileInputStream(path)
          wb = WorkbookFactory.create(in)
          sheet = wb.getSheetAt(0)
          rowsNum = sheet.getLastRowNum
          rows = for (i <- 0 to rowsNum) yield sheet.getRow(i)
        case Failure(e) => throw new IllegalStateException(e)
      }
    }
  }

  def path = filePath

  def saveQuestion(idx: Int, question: Question): Unit = {
    init()
    val out = new FileOutputStream(filePath)
    val row = sheet.getRow(idx + 1)
    question toRow row
    wb.write(out)
    out.close()
  }

  def getQuestions: Seq[Question] = {
    init()
    rows.drop(1).map(row => toQuestion(row)).filterNot(_.status == Question.Status.ASKED)
  }

  def makeAskedQuestionNumber(qNum: Int): Unit = {
    init()
    val out = new FileOutputStream(filePath)
    val row = sheet.getRow(qNum + 1)
    row.getCell(statusCell, Row.CREATE_NULL_AS_BLANK).setCellValue(Question.Status.ASKED)
    wb.write(out)
    out.close()
  }

  private def getVariants(row: Row) = {
    var variants = scala.collection.mutable.Map[String, String]()
    var idx = 1
    for (i <- 3 to 6) {
      val str = row.getCell(i).getStringCellValue
      variants += (idx.toString -> str)
      idx = idx + 1
    }
    variants.toMap
  }

  private def toQuestion(row: Row): Question = {
    val num: Int = row.getCell(0).getNumericCellValue.toInt
    val name: String = row.getCell(1).getStringCellValue
    val question: String = row.getCell(2).getStringCellValue
    val variants = getVariants(row)
    val correctVar = row.getCell(7).getNumericCellValue.toShort
    val answer = row.getCell(8).getStringCellValue
    val status = row.getCell(9).getStringCellValue
    val img1 = ""
    val img2 = ""
    Question(num, name, question, correctVar, answer, status, img1, img2, variants)
  }
}

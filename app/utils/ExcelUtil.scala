package utils

import java.io.{File, FileInputStream, FileOutputStream}

import model.Question
import org.apache.poi.ss.usermodel.{Row, WorkbookFactory}

/**
  * Created by ibes on 02.02.17.
  */
object ExcelUtil {
  val cellsQuantity = 12 - 1
  case class ExcelReader(filePath: String) {
    // TODO: push Fileobject here
    require(filePath.nonEmpty, "Empty file path")
    require(new File(filePath).exists(), s"File at path $filePath not found")

    val in = new FileInputStream(filePath)
    val wb = WorkbookFactory.create(in)
    val sheet = wb.getSheetAt(0)
    val rowsNum = sheet.getLastRowNum
    val rows = for(i <- 0 to rowsNum) yield sheet.getRow(i)
//    val row1 = sheet.getRow(2)
//    val rowLength = row1.getLastCellNum

//    val cells: IndexedSeq[Cell] = for(i <- 0 to cellsQuantity) yield
//      row1.getCell(i, Row.CREATE_NULL_AS_BLANK)
//    println("cells")


    def saveQuestion(idx: Int, question: Question) = {
      val out = new FileOutputStream(filePath)
      val row = sheet.getRow(idx + 1)
      question toRow row
      wb.write(out)
      out.close()
    }

    def getQuestions: Seq[Question] = {
      rows.drop(1).map(row => toQuestion(row))
    }

    def getVariants(row: Row) = {
      var variants = scala.collection.mutable.Map[String, String]()
      var idx = 1
      for(i <- 3 to 6) {
        val str = row.getCell(i).getStringCellValue
        variants += (idx.toString -> str)
        idx = idx + 1
      }
      variants.toMap
    }

    def toQuestion(row: Row): Question = {
      val num: Int = row.getCell(0).getNumericCellValue.toInt
      val name: String = row.getCell(1).getStringCellValue
      val question: String = row.getCell(2).getStringCellValue
      val variants = getVariants(row)
      val correctVar = row.getCell(7).getNumericCellValue.toShort
      val answer = row.getCell(8).getStringCellValue
      val status = row.getCell(9).getNumericCellValue.toInt
      val img1 = ""
      val img2 = ""
      Question(num, name, question, correctVar, answer, status, img1, img2, variants)
    }
  }

}

package utils

import java.io.{File, PrintWriter}

import scala.io.Source
import scala.util.Try

/**
  * Created by ibes on 02.02.17.
  */
object FileUtils {
  private  val PATH = "public/persist/excel.path"
  def persistExcelFilePath(path: String): Unit = {
    val pw = new PrintWriter(new File(PATH))
    pw.write(path)
    pw.close()
  }

  def getExcelFilePath: Try[String] = Try(Source.fromFile(PATH).getLines().mkString)
}

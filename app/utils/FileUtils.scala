package utils

import java.io.{File, PrintWriter}
import java.nio.charset.Charset

import scala.io.Source
import scala.util.{Failure, Success, Try}

/**
  * Created by ibes on 02.02.17.
  */
object FileUtils {
  private  val PATH = "public/persist/excel.path"
  def persistExcelFilePath(path: String) = {
    val pw = new PrintWriter(new File(PATH))
    pw.write(path)
    pw.close()
  }

  def getExcelFilePath: Try[String] = Try(Source.fromFile(PATH).getLines().mkString)
}

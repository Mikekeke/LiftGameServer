package utils

import java.io.{File, PrintWriter}

import play.api.Configuration

import scala.io.Source
import scala.util.Try

/**
  * Created by ibes on 02.02.17.
  */
import javax.inject._
@Singleton
class MyFileUtils @Inject()(configuration: Configuration) {
  private  val PATH = s"${configuration.getString("uploadDir")}/excel.path"
  def persistExcelFilePath(path: String, dir: String): Unit = {
    val pw = new PrintWriter(new File(PATH))
    pw.write(path)
    pw.close()
  }

  def getExcelFilePath: Try[String] = Try(Source.fromFile(PATH).getLines().mkString)
}

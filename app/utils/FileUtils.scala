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
class FileUtils @Inject()(configuration: Configuration) {
  private  val PATH: String = s"${configuration.getString("uploadDir")
    .getOrElse(throw new NoSuchElementException("Cant find path in config"))}/excel.path"
  def persistExcelFilePath(path: String): Unit = {
    val pw = new PrintWriter(new File(PATH))
    pw.write(path)
    pw.close()
  }

  def getExcelFilePath: Try[String] = Try(Source.fromFile(PATH).getLines().mkString)
}

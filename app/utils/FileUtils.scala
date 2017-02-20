package utils

import java.io.{File, FileNotFoundException, PrintWriter}

import play.api.Configuration

import scala.io.Source
import scala.util.Try

/**
  * Created by ibes on 02.02.17.
  */
import javax.inject._
@Singleton
class FileUtils @Inject()(configuration: Configuration) {

  private val PATH = {configuration.getString("uploadDir")
    .getOrElse(throw new NoSuchElementException("Cant find path in config"))}
  private  val EXCEL_PATH: String = s"$PATH/excel.path"

  def persistExcelFilePath(path: String): Unit = {
    val pw = new PrintWriter(new File(EXCEL_PATH))
    pw.write(path)
    pw.close()
  }

  def getExcelFilePath: Try[String] = Try(Source.fromFile(EXCEL_PATH).getLines().mkString)
  def getUploadDir: String = PATH

  def getImagesDir: Try[String] = {
    Try(
      configuration.getString("uploadDir").getOrElse(throw new FileNotFoundException("No upload dir found")) +
        configuration.getString("imagesDir").getOrElse(throw new FileNotFoundException("No image dir found"))
    )
  }
}

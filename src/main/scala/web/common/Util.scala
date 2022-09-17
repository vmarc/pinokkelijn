package web.common

import org.apache.commons.io.FileUtils

import java.io.File
import java.nio.charset.Charset
import scala.jdk.CollectionConverters._

object Util {

  def mkdir(dirname: String): Unit = {
    FileUtils.forceMkdir(new File(dirname))
  }

  def listFiles(sourceDirectory: String, extensions: Seq[String], recursive: Boolean = false): Seq[File] = {
    FileUtils.listFiles(new File(sourceDirectory), extensions.toArray, recursive).asScala.toSeq
  }

  def copyFile(sourceFile: File, destinationFile: File): Unit = {
    FileUtils.copyFile(sourceFile, destinationFile)
  }

  def stringToFile(string: String, filename: String): Unit = {
    FileUtils.writeStringToFile(new File(filename), string, Charset.forName("UTF-8"))
  }

  def exists(filename: String): Boolean = {
    new File(filename).exists()
  }
}

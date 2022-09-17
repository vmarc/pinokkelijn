package web.common

import org.apache.commons.io.FileUtils
import web.view.Triplet

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

  def slide[A](list: Seq[A]): Seq[Triplet[A]] = {
    list.indices.map { index =>
      val current = list(index)
      val previous = if (index == 0) None else Some(list(index - 1))
      val next = if (index < (list.size - 1)) Some(list(index + 1)) else None
      Triplet[A](previous, current, next)
    }
  }
}

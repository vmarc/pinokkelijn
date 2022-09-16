package web.tools

import org.apache.commons.io.FileUtils

import java.io.File
import scala.jdk.CollectionConverters._

object FileRenameTool {

  def main(args: Array[String]): Unit = {
    val sourceDirectory = "/share/pinokkelijn/zwanenzang-zwijnerij"
    val destinationDirectory = "/home/marcv/wrk/projects3/web/wrk/images/productions/2018/gallery"
    new FileRenameTool(sourceDirectory, destinationDirectory).rename()
  }
}

class FileRenameTool(sourceDirectory: String, destinationDirectory: String) {
  def rename(): Unit = {
    val images = FileUtils.listFiles(new File(sourceDirectory), Array("jpg"), false).asScala
    println(images.size)
    images.foreach { image =>
      val newName = image.getName.replaceAll("M&T", "")
      val destination = new File(destinationDirectory, newName)
      println(s"${image.getAbsolutePath} -> ${destination.getAbsolutePath}")
      FileUtils.copyFile(image, destination)
    }
  }
}

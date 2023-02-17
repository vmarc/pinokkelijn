package web.tools

import web.common.Util.copyFile
import web.common.Util.listFiles

import java.io.File

object FileRenameTool {

  def main(args: Array[String]): Unit = {
    val sourceDirectory = "/share/fotos/pinokkelijn"
    val destinationDirectory = "/home/marcv/wrk/projects3/web/wrk/images/productions/2022/gallery"
    new FileRenameTool(sourceDirectory, destinationDirectory).rename()
  }
}

class FileRenameTool(sourceDirectory: String, destinationDirectory: String) {
  def rename(): Unit = {
    val images = listFiles(sourceDirectory, Seq("jpg", "JPG")).sortWith { case (a ,b) =>
      val x = a.getName.dropRight(4).toInt
      val y = b.getName.dropRight(4).toInt
      x < y
    }
    println(images.size)
    images.zipWithIndex.foreach { case (image, index) =>
      // val newName = image.getName.replaceAll("M&T", "").toLowerCase()
      val newName = String.format("2022-%03d.jpg", index + 1)
      val destination = new File(destinationDirectory, newName)
      println(s"${image.getAbsolutePath} -> ${destination.getAbsolutePath}")
      copyFile(image, destination)
    }
  }
}

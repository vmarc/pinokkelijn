package web.tools

import web.common.Util.copyFile
import web.common.Util.listFiles

import java.io.File

object FileRenameTool {

  def main(args: Array[String]): Unit = {
    val sourceDirectory = "/share/pinokkelijn/zwanenzang-zwijnerij"
    val destinationDirectory = "/home/marcv/wrk/projects3/web/wrk/images/productions/2018/gallery"
    new FileRenameTool(sourceDirectory, destinationDirectory).rename()
  }
}

class FileRenameTool(sourceDirectory: String, destinationDirectory: String) {
  def rename(): Unit = {
    val images = listFiles(sourceDirectory, Seq("jpg"))
    println(images.size)
    images.foreach { image =>
      val newName = image.getName.replaceAll("M&T", "")
      val destination = new File(destinationDirectory, newName)
      println(s"${image.getAbsolutePath} -> ${destination.getAbsolutePath}")
      copyFile(image, destination)
    }
  }
}

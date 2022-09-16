package web.tools

import org.apache.commons.io.FileUtils
import web.domain.ImageDimension
import web.view.Images

import java.io.File
import java.io.FileWriter
import java.io.PrintWriter
import javax.imageio.ImageIO
import scala.jdk.CollectionConverters._
import scala.xml.XML

/**
 * Determines the dimensions of the images in the staging area.
 */
class ImageParser(config: SiteBuilderOptions) {

  def parse(): Images = {
    val cachedDimensions = loadCachedDimensions().toMap
    val missingDimensions = readMissingDimensions(cachedDimensions)
    val dimensions = cachedDimensions ++ missingDimensions
    if (missingDimensions.nonEmpty) {
      save(dimensions)
    }
    new Images(config, dimensions)
  }

  private def cacheFilename: String = {
    new File(config.stagingDir + "/images.xml").getAbsolutePath
  }

  private def loadCachedDimensions(): Seq[(String, ImageDimension)] = {
    if (!new File(cacheFilename).exists) {
      Nil
    }
    else {
      val images = XML.loadFile(cacheFilename)
      (images \ "image").map { image =>
        val name = (image \ "@name").text
        val width = (image \ "@width").text
        val height = (image \ "@height").text
        name -> ImageDimension(width, height)
      }
    }
  }

  private def readMissingDimensions(cachedDimensions: Map[String, ImageDimension]): Seq[(String, ImageDimension)] = {
    val missingFiles = imageFiles.filterNot { filename => cachedDimensions.contains(filename) }
    if (missingFiles.isEmpty) {
      Nil
    }
    else {
      printf("Analyzing %d missing image dimensions\n", missingFiles.size)
      val dims = missingFiles.zipWithIndex.map { case (filename, index) =>
        if (index % 100 == 0) {
          printf("Analyzing missing image %d/%d\n", index, missingFiles.size)
        }
        filename -> newDimension(filename)
      }
      printf("%d image dimensions analyzed\n", missingFiles.size)
      dims
    }
  }

  private def newDimension(imageFilename: String) = {
    val filename = config.rootDir + imageFilename
    val image = try {
      ImageIO.read(new File(filename))
    }
    catch {
      case e: Exception =>
        throw new RuntimeException(s"Could not read image '$filename'", e)
    }
    ImageDimension(image.getWidth().toString, image.getHeight().toString)
  }

  private def imageFiles: Seq[String] = {
    val root = config.rootDir
    val files = FileUtils.listFiles(new File(root), Array("gif", "png", "jpg"), true).asScala.toSeq
    files.map(file => file.getAbsolutePath).filterNot(_.contains("highslide")).map { filename =>
      filename.substring(root.length)
    }
  }

  private def save(dimensions: Map[String, ImageDimension]): Unit = {
    val out = new PrintWriter(new FileWriter(cacheFilename))
    try {
      out.println("<images>")
      sorted(dimensions).foreach { case (filename, dimension) =>
        out.printf("   <image name=\"%s\" width=\"%s\" height=\"%s\" />\n", filename, dimension.width, dimension.height)
      }
      out.println("</images>")
    } finally {
      out.close()
    }
  }

  private def sorted(dimensions: Map[String, ImageDimension]): Seq[(String, ImageDimension)] = {
    dimensions.keys.toList.sorted.map(key => key -> dimensions(key))
  }
}

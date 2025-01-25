package web.tools

import web.common.Util.copyFile
import web.common.Util.exists
import web.common.Util.listFiles
import web.domain.Site

import java.io.File
import java.io.FileFilter

object ImageCopy {
  def main(args: Array[String]): Unit = {
    val config = SiteBuilderOptions()
    val site = new SiteParser(config.xmlDir).site()
    println(">>> copies")
    new ImageCopy(site, config).productionPhotoCopies foreach println
    println("<<< copies")
  }
}

case class CopiedImage(source: String, destination: String)

/**
 * Copies images from the working to the staging
 * directories.
 */
class ImageCopy(site: Site, options: SiteBuilderOptions) {

  def build(): Unit = {
    val copies = productionPhotoCopies ++ homeImageCopies ++ otherFiles
    val filtered = copies.filterNot { copy => exists(copy.destination) }
    filtered.foreach(cp)
    printf("File copy: total number of files: %d, files copied (or atttempted to copy): %d\n", copies.size, filtered.size)
  }

  private def otherFiles: Seq[CopiedImage] = {
    val sourceDir = "/Users/marc/wrk/projects/web/wrk/"
    listFiles(sourceDir + "highslide", recursive = true).map { sourceFile =>
      val source = sourceFile.getAbsolutePath
      val destination = options.rootDir + source.substring(sourceDir.length)
      CopiedImage(source, destination)
    }
  }

  private def homeImageCopies: Seq[CopiedImage] = {
    val sourceDir = options.imageSourceDir
    val images = imageFiles(new File(sourceDir))
    images.map { image =>
      val source = image.getAbsolutePath
      val destination = options.rootDir + "images/" + image.getAbsolutePath.substring(sourceDir.length)
      CopiedImage(source, destination)
    }
  }

  private def productionPhotoCopies: Seq[CopiedImage] = {
    site.productions.flatMap { production =>
      val dir = new File(options.productionSourceDir(production))
      if (dir.exists) {
        val images = imageFiles(dir)
        images.filterNot(_.getName.contains("poster")).map { file =>
          val src = file.getAbsolutePath
          val dst = options.dir(production.id) + file.getName
          CopiedImage(src, dst)
        }
      }
      else {
        Seq.empty
      }
    }
  }

  private def cp(copy: CopiedImage): Unit = {
    val path = new File(copy.source)
    val dest = new File(copy.destination)
    copyFile(path, dest)
  }

  private def imageFiles(dir: File): Seq[File] = {
    dir.listFiles(new FileFilter() {
      override def accept(pathname: File): Boolean = {
        pathname.getName.endsWith("jpg") ||
          pathname.getName.endsWith("gif") ||
          pathname.getName.endsWith("png")
      }
    }).toSeq
  }
}

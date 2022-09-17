package web.tools

import web.common.Util.copyFile
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
class ImageCopy(site: Site, config: SiteBuilderOptions) {

  def build(): Unit = {
    val copies = productionPhotoCopies ++ personPhotoCopies ++ homeImageCopies ++ otherFiles
    val filtered = copies.filterNot { copy => new File(copy.destination).exists }
    filtered.foreach(cp)
    printf("File copy: total number of files: %d, files copied (or atttempted to copy): %d\n", copies.size, filtered.size)
  }

  def otherFiles: Seq[CopiedImage] = {
    // TODO
    //  Path.fromString("wrk/highslide").descendants().toList.map { src =>
    //    new CopiedImage(src.path, src.path.replace("wrk/", config.rootDir))
    //  }
    Seq.empty
  }

  private def homeImageCopies: Seq[CopiedImage] = {
    val dirs = Seq("images" /*, "images/fons", "images/richard", "images/seppe", "images/maan", "images/festival", "images/yvonne"*/)
    val images = dirs.map("wrk/" + _).map(new File(_)).flatMap(imageFiles)
    images.map { image =>
      val source = image.getAbsolutePath
      val destination = config.rootDir + image.getAbsolutePath.substring((config.sourceDir + "/wrk/").length)
      CopiedImage(source, destination)
    }
  }

  private def productionPhotoCopies: Seq[CopiedImage] = {
    site.productions.flatMap { production =>
      val dir = new File(config.productionSourceDir(production))
      if (dir.exists) {
        val images = imageFiles(dir)
        images.filterNot(_.getName.contains("poster")).map { file =>
          val src = file.getAbsolutePath
          val dst = config.dir(production.id) + file.getName
          CopiedImage(src, dst)
        }
      }
      else {
        Seq.empty
      }
    }
  }

  private def personPhotoCopies: Seq[CopiedImage] = {
    val personsWithPhoto = site.persons.filter { person => new File(config.personSourcePhoto(person)).exists }
    personsWithPhoto.map { person =>
      val src = config.personSourcePhoto(person)
      val dst = config.largePersonPhoto(person)
      CopiedImage(src, dst)
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

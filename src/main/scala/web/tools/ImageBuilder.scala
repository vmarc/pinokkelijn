package web.tools

import web.domain.Person
import web.domain.Photo
import web.domain.Production
import web.domain.Site

import java.io.File
import java.io.InputStream
import java.io.StringWriter

class ImageBuilder(site: Site, config: SiteBuilderOptions) {

  def build(): Unit = {
    convert(todoPhotos)

    todoPosterProductions foreach { production =>
      convert(config.posterSourceFile(production), config.smallPosterFile(production), "100x100")
      convert(config.posterSourceFile(production), config.largePosterFile(production), "200x500")
      convert(config.posterSourceFile(production), config.posterFile(production), "400x1000")
    }

    todoPersonPhotos foreach { person =>
      convert(config.personSourcePhoto(person), config.smallPersonPhotoFile(person), "80x130")
      convert(config.personSourcePhoto(person), config.largePersonPhotoFile(person), "200x500")
    }
  }

  private def todoPosterProductions: Seq[Production] = {
    site.productions.filter { production =>
      val src = config.posterSourceFile(production)
      if (exists(src)) {
        !exists(config.smallPosterFile(production)) ||
          !exists(config.largePosterFile(production))
      } else {
        false
      }
    }
  }

  private def todoPersonPhotos: Seq[Person] = {
    site.persons.filter { person =>
      val src = config.personSourcePhoto(person)
      if (exists(src)) {
        !exists(config.smallPersonPhotoFile(person)) ||
          !exists(config.largePersonPhotoFile(person))
      } else {
        false
      }
    }
  }

  private def todoPhotos: Seq[Photo] = {
    site.productionPhotos(config.isWeb).filter { photo =>
      val src = config.photoFile(photo)
      if (exists(src)) {
        !exists(config.smallPhotoFile(photo)) ||
          !exists(config.largePhotoFile(photo))
      } else {
        println("ERROR: file does not exist: " + src)
        false
      }
    }
  }

  private def convert(photos: Seq[Photo]): Unit = {
    photos.zipWithIndex foreach {
      case (photo, index) =>
        if ((index % 25) == 0) {
          printf("%d/%d\n", index, photos.size)
        }
        convert(config.photoFile(photo), config.smallPhotoFile(photo), "100x100")
        convert(config.photoFile(photo), config.largePhotoFile(photo), "500x500")
    }
  }

  private def convert(source: String, destination: String, geometry: String): Unit = {

    println(destination)

    val command = "convert -verbose -antialias -geometry %s %s %s".format(geometry, source, destination)
    val process = Runtime.getRuntime.exec(command)

    val exitValue = process.waitFor()
    if (exitValue > 0) {
      println("Command failed: " + command)
      println("exitValue=" + exitValue)
      val error = toString(process.getErrorStream)
      val output = toString(process.getInputStream)

      if (error.nonEmpty) {
        println("--- error ---")
        println(error)
      }
      if (output.nonEmpty) {
        println("--- outpout ---")
        println(output)
        println("---")
      }
      System.exit(exitValue)
    }
  }

  private def toString(s: InputStream): String = {
    val sw = new StringWriter()
    while (s.available() > 0) {
      sw.write(s.read())
    }
    sw.toString
  }

  private def exists(filename: String): Boolean = {
    new File(filename).exists()
  }
}

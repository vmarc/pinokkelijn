package web.tools

import org.apache.commons.io.FileUtils
import web.domain.Person
import web.domain.PersonDetail
import web.domain.Photo
import web.domain.Production
import web.domain.Site
import web.server.engine.PageGenerator
import web.server.mail.HtmlPrettyPrinter
import web.server.sitemap.SiteMapBuilder
import web.server.sitemap.SiteMapReader
import web.server.sitemap.SiteMapUrl
import web.server.sitemap.SiteMapWriter
import web.view.PersonInfo

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import scala.collection.mutable.ListBuffer

object SiteBuilderTool {
  def main(args: Array[String]): Unit = {
    val options = new SiteBuilderOptionsParser().options(args)
    val site = new SiteParser(options.xmlDir).site()
    new SiteBuilderTool(site, options).make()
    println("Ready")
  }
}

class SiteBuilderTool(site: Site, options: SiteBuilderOptions) {

  private val updatedUrls = List("index.html", "producties.html", "personen-lijst.html").map(_.r) // can include wildcards; these url's will get the current system time as lastModifiedTime
  private val oldSiteMap: Map[String, SiteMapUrl] = readOldSiteMap()
  private val siteMapBuilder = new SiteMapBuilder(updatedUrls, oldSiteMap)

  val htmlPrettyPrinter = new HtmlPrettyPrinter()
  private val pg = new PageGenerator(htmlPrettyPrinter)
  private val images = new ImageParser(options).parse()

  def make(): Unit = {

    new DirectoryBuilder(site, options).build()

    copyStaticContents()

    new ImageCopy(site, options).build()

    if (options.images) {
      new ImageBuilder(site, options).build()
    }

    generateFaceBook()

    if (options.root) {
      makeIndexPage()
      makeRootPages()
    }

    if (options.productions) {
      makeProductionsPages()
      slide(site.productions) foreach { productionTriplet =>
        makePage(productionTriplet)
      }
    }

    if (options.persons) {
      makePersonsPages()
      slide(site.persons) foreach { personTriplet =>
        val person = personTriplet.current
        makePersonPage(personTriplet)
      }
    }

    val siteMap: Seq[SiteMapUrl] = siteMapBuilder.build()
    val out = new PrintWriter(new FileWriter(options.rootDir + "/sitemap.xml"))
    new SiteMapWriter(out).write(siteMap)
    out.close()
  }

  private def readOldSiteMap(): Map[String, SiteMapUrl] = {
    val reader = new FileReader(options.sourceDir + "/wrk/xml/sitemap.xml")
    try {
      new SiteMapReader(reader).read()
    }
    finally {
      reader.close()
    }
  }

  private def makeIndexPage(): Unit = {
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
    )

    merge(context, "index.ssp", options.rootDir + "index.html")
  }

  private def merge(context: Map[String, Any], templateName: String, outputFilename: String): Unit = {
    siteMapBuilder.addUrl(outputFilename.substring("/home/marcv/wrk/web/staging/web/".length)) // TODO clean up literal reference
    val result = pg.generate(context, templateName)
    FileUtils.writeStringToFile(new File(outputFilename), result, Charset.forName("UTF-8"))
  }

  private def makeRootPages(): Unit = {
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "site" -> site
    )
    listFiles("wrk/pages").filter(_.endsWith(".ssp")) foreach { filename =>
      val output = options.rootDir + filename.replace(".ssp", "")
      merge(context, filename, output)
    }
  }

  private def makeProductionsPages(): Unit = {
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "productions" -> site.productions.reverse
    )
    merge(context, "templates/productions.ssp", options.rootDir + "producties.html")
    merge(context, "templates/posters.ssp", options.rootDir + "posters.html")
  }

  private def makePersonsPages(): Unit = {
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "letterPersonsCollection" -> site.letterPersonsCollection
    )
    merge(context, "templates/persons.ssp", options.rootDir + "personen-lijst.html")
  }

  private def makePersonPage(personTriplet: Triplet[Person]): Unit = {

    val person = personTriplet.current
    val details: Seq[PersonDetail] = if (options.isWeb) person.webDetails else person.details
    val personInfo = PersonInfo(person, personTriplet.previous, personTriplet.next, details)

    val context = Map[String, Any](
      "root" -> "..",
      "images" -> images,
      "info" -> personInfo
    )
    merge(context, "templates/person.ssp", options.personsDir + person.key + ".html")
  }

  private def makePage(productions: Triplet[Production]): Unit = {
    println("Generating " + productions.current.id)
    val photos = if (options.isWeb) {
      productions.current.photos.filter(_.web)
    } else {
      productions.current.photos
    }
    makePage(productions, photos)
  }

  private def makePage(productions: Triplet[Production], photos: Seq[Photo]): Unit = {
    val context = Map[String, Any](
      "root" -> "..",
      "images" -> images,
      "root" -> "../..",
      "production" -> productions.current,
      "previousProduction" -> productions.previous,
      "nextProduction" -> productions.next,
      "photos" -> photos
    )
    merge(context, "templates/production.ssp", options.dir(productions.current.id) + "index.html")
    merge(context, "templates/productionPhotos.ssp", options.dir(productions.current.id) + "photos.html")
  }

  private def copyStaticContents(): Unit = {
    listFiles("wrk/static").foreach { filename =>
      val source = new File("wrk/static/" + filename)
      val destination = new File(options.rootDir + filename)
      FileUtils.copyFile(source, destination)
    }
  }

  private def slide[A](list: Seq[A]): Seq[Triplet[A]] = {
    val result = new ListBuffer[Triplet[A]]()
    list.indices.foreach { index =>
      val current = list(index)
      val previous = if (index == 0) None else Some(list(index - 1))
      val next = if (index < (list.size - 1)) Some(list(index + 1)) else None
      result += new Triplet[A](previous, current, next)
    }
    result.toList
  }

  private def listFiles(dir: String): Seq[String] = {
    new File(dir).list().filterNot(_.endsWith(".svn")).sorted
  }

  private def generateFaceBook(): Unit = {
    val persons = site.persons.filter(images.hasPerson).sortWith(popularity)
    val personInfos = persons.map { person =>
      val details: Seq[PersonDetail] = if (options.isWeb) person.webDetails else person.details
      PersonInfo(person, None, None, details)
    }
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "personInfos" -> personInfos
    )
    merge(context, "templates/faces.ssp", options.rootDir + "/personen-koppen.html")
  }

  private def popularity(person1: Person, person2: Person): Boolean = {
    if (person1.details.head.production.id > person2.details.head.production.id) {
      true
    }
    else if (person1.details.head.production.id < person2.details.head.production.id) {
      false
    }
    else {

      if (person1.details.size > person2.details.size) {
        true
      }
      else if (person1.details.size < person2.details.size) {
        false
      }
      else {
        person1.fullName < person2.fullName
      }
    }
  }
}

case class Triplet[A](previous: Option[A], current: A, next: Option[A])

package web.tools

import web.common.Util.copyFile
import web.common.Util.listFiles
import web.common.Util.slide
import web.common.Util.stringToFile
import web.domain.Person
import web.domain.PersonDetail
import web.domain.Photo
import web.domain.Production
import web.domain.Site
import web.server.engine.PageBuilder
import web.server.mail.HtmlPrettyPrinter
import web.server.sitemap.SiteMapBuilder
import web.server.sitemap.SiteMapReader
import web.server.sitemap.SiteMapUrl
import web.server.sitemap.SiteMapWriter
import web.view.PersonInfo
import web.view.Triplet

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.PrintWriter

object SiteBuilderTool {
  def main(args: Array[String]): Unit = {
    val options = new SiteBuilderOptionsParser().options(args)
    val site = new SiteParser(options.xmlDir).site()
    new SiteBuilderTool(site, options).build()
    println("Ready")
  }
}

class SiteBuilderTool(site: Site, options: SiteBuilderOptions) {

  // can include wildcards; these url's will get the current system time as lastModifiedTime
  private val updatedUrls = List("index.html", "producties.html", "personen-lijst.html").map(_.r)
  private val oldSiteMap: Map[String, SiteMapUrl] = readOldSiteMap()
  private val siteMapBuilder = new SiteMapBuilder(updatedUrls, oldSiteMap)

  val htmlPrettyPrinter = new HtmlPrettyPrinter()
  private val pageBuilder = new PageBuilder(htmlPrettyPrinter)
  private val images = new ImageParser(options).parse()

  def build(): Unit = {

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
      slide(site.productions).foreach { productionTriplet =>
        makePage(productionTriplet)
      }
    }

    if (options.persons) {
      makePersonsPages()
      slide(site.persons).foreach { personTriplet =>
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

    build(context, "templates/index.ssp", options.rootDir + "index.html")
  }

  private def build(context: Map[String, Any], templateName: String, outputFilename: String): Unit = {
    siteMapBuilder.addUrl(outputFilename.substring(options.rootDir.length + 1))
    val result = pageBuilder.build(context, templateName)
    stringToFile(result, outputFilename)
  }

  private def makeRootPages(): Unit = {
    val sourceDir = options.sourceDir + "/wrk/pages"
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "site" -> site
    )
    build(context, "templates/leden.ssp", options.rootDir + "leden.html")
    build(context, "templates/intro.ssp", options.rootDir + "intro.html")
    build(context, "templates/reaction.ssp", options.rootDir + "reaction.html")
    build(context, "templates/reservation.ssp", options.rootDir + "reservation.html")
  }

  private def makeProductionsPages(): Unit = {
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "productions" -> site.productions.reverse
    )
    build(context, "templates/productions.ssp", options.rootDir + "producties.html")
    build(context, "templates/posters.ssp", options.rootDir + "posters.html")
  }

  private def makePersonsPages(): Unit = {
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "letterPersonsCollection" -> site.letterPersonsCollection
    )
    build(context, "templates/persons.ssp", options.rootDir + "personen-lijst.html")
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
    build(context, "templates/person.ssp", options.personsDir + person.key + ".html")
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
    build(context, "templates/production.ssp", options.dir(productions.current.id) + "index.html")
    build(context, "templates/production-photos.ssp", options.dir(productions.current.id) + "photos.html")
  }

  private def copyStaticContents(): Unit = {
    val sourceDir = "src/main/resources/static/"
    listFiles(sourceDir).map(_.getName).foreach { filename =>
      val source = new File(sourceDir + filename)
      val destination = new File(options.rootDir + filename)
      copyFile(source, destination)
    }
  }

  private def generateFaceBook(): Unit = {
    val persons = site.persons.filter(images.hasPerson).sortWith(Person.compareByPopularity)
    val personInfos = persons.map { person =>
      val details: Seq[PersonDetail] = if (options.isWeb) person.webDetails else person.details
      PersonInfo(person, None, None, details)
    }
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "personInfos" -> personInfos
    )
    build(context, "templates/faces.ssp", options.rootDir + "/personen-koppen.html")
  }
}

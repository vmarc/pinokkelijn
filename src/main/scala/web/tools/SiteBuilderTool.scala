package web.tools

import org.jsoup.Jsoup
import play.twirl.api.Html
import play.twirl.api.Template1
import play.twirl.api.Template3
import web.common.Templates
import web.common.Util.copyFile
import web.common.Util.listFiles
import web.common.Util.slide
import web.common.Util.stringToFile
import web.domain.Person
import web.domain.PersonDetail
import web.domain.Photo
import web.domain.Production
import web.domain.Site
import web.pages.FacesPage
import web.pages.IndexPage
import web.pages.IntroPage
import web.pages.LedenPage
import web.pages.PersonPage
import web.pages.PersonsPage
import web.pages.PostersPage
import web.pages.ProductionPage
import web.pages.ProductionPhotosPage
import web.pages.ProductionsPage
import web.pages.ReactionPage
import web.pages.ReservationPage
import web.server.sitemap.SiteMapBuilder
import web.server.sitemap.SiteMapReader
import web.server.sitemap.SiteMapUrl
import web.server.sitemap.SiteMapWriter
import web.view.Images
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
    val reader = new FileReader(options.xmlDir + "/sitemap.xml")
    try {
      new SiteMapReader(reader).read()
    }
    finally {
      reader.close()
    }
  }

  private def makeIndexPage(): Unit = {
    val page = IndexPage(".")

    val contentTemplate = Templates.getTemplate[Template1[IndexPage, Html]]("html.index")
    val contents = contentTemplate.render(page)
    val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
    val string = pageTemplate.render(page, images, contents)

    val url = "/index.html"
    val outputFilename = options.rootDir + url

    siteMapBuilder.addUrl(url)
    val prettyString = Jsoup.parse(string.toString()).html()
    stringToFile(prettyString, outputFilename)
  }

  private def makeRootPages(): Unit = {

    {
      val page = LedenPage(".")

      val contentTemplate = Templates.getTemplate[Template1[LedenPage, Html]]("html.leden")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "/leden.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }

    {
      val page = IntroPage(".")

      val contentTemplate = Templates.getTemplate[Template1[IntroPage, Html]]("html.intro")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "/intro.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }

    {
      val page = ReactionPage(".")

      val contentTemplate = Templates.getTemplate[Template1[ReactionPage, Html]]("html.reaction")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "/reaction.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }

    {
      val page = ReservationPage(".")

      val contentTemplate = Templates.getTemplate[Template1[ReservationPage, Html]]("html.reservation")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "/reservation.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }
  }

  private def makeProductionsPages(): Unit = {

    {
      val page = ProductionsPage(
        ".",
        site.productions.reverse
      )

      val contentTemplate = Templates.getTemplate[Template1[ProductionsPage, Html]]("html.productions")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "/producties.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }

    {
      val page = PostersPage(
        ".",
        site.productions.reverse,
        images
      )

      val contentTemplate = Templates.getTemplate[Template1[PostersPage, Html]]("html.posters")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "/posters.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }
  }

  private def makePersonsPages(): Unit = {

    val page = PersonsPage(
      ".",
      site.letterPersonsCollection
    )

    val contentTemplate = Templates.getTemplate[Template1[PersonsPage, Html]]("html.persons")
    val contents = contentTemplate.render(page)
    val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
    val string = pageTemplate.render(page, images, contents)

    val url = "/personen-lijst.html"
    val outputFilename = options.rootDir + url

    siteMapBuilder.addUrl(url)

    val prettyString = Jsoup.parse(string.toString()).html()
    stringToFile(prettyString, outputFilename)
  }

  private def makePersonPage(personTriplet: Triplet[Person]): Unit = {

    val person = personTriplet.current
    val details: Seq[PersonDetail] = if (options.isWeb) person.webDetails else person.details
    val personInfo = PersonInfo(person, personTriplet.previous, personTriplet.next, details)

    val page = PersonPage(
      "..",
      personInfo,
      images
    )

    val contentTemplate = Templates.getTemplate[Template1[PersonPage, Html]]("html.person")
    val contents = contentTemplate.render(page)
    val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
    val string = pageTemplate.render(page, images, contents)

    val url = s"/personen/${person.key}.html"
    val outputFilename = options.rootDir + url

    siteMapBuilder.addUrl(url)
    val prettyString = Jsoup.parse(string.toString()).html()
    stringToFile(prettyString, outputFilename)
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

    {
      val page = ProductionPage(
        "../..",
        productions.current,
        productions.previous,
        productions.next,
        photos,
        images
      )

      val contentTemplate = Templates.getTemplate[Template1[ProductionPage, Html]]("html.production")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "producties/" + productions.current.id + "/index.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }

    {
      val page = ProductionPhotosPage(
        "../..",
        productions.current,
        productions.previous,
        productions.next,
        photos,
        images
      )

      val contentTemplate = Templates.getTemplate[Template1[ProductionPhotosPage, Html]]("html.productionPhotos")
      val contents = contentTemplate.render(page)
      val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
      val string = pageTemplate.render(page, images, contents)

      val url = "producties/" + productions.current.id + "/photos.html"
      val outputFilename = options.rootDir + url

      siteMapBuilder.addUrl(url)
      val prettyString = Jsoup.parse(string.toString()).html()
      stringToFile(prettyString, outputFilename)
    }
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

    val page = FacesPage(
      personInfos,
      images,
      "."
    )

    val contentTemplate = Templates.getTemplate[Template1[FacesPage, Html]]("html.faces")
    val contents = contentTemplate.render(page)
    val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
    val string = pageTemplate.render(page, images, contents)

    val url = "/personen-koppen.html"
    val outputFilename = options.rootDir + url

    siteMapBuilder.addUrl(url)
    val prettyString = Jsoup.parse(string.toString()).html()
    stringToFile(prettyString, outputFilename)
  }
}

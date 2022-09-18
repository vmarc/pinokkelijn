package web.server.sitemap

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import web.domain.Day

class SiteMapBuilderTest extends AnyFunSuite with Matchers {

  test("test site map builder") {

    val oldSiteMap = Map(
      "test1.html" -> SiteMapUrl("test1.html", "2012-01-01", "bla", "bla"),
      "test2.html" -> SiteMapUrl("test2.html", "2012-01-01", "bla", "bla"),
      "producties.html" -> SiteMapUrl("producties.html", "2012-01-01", "bla", "bla")
    )
    val updatedUrls = List("test1.html").map(_.r)

    Day.set("2012-08-11")

    val b = new SiteMapBuilder(updatedUrls, oldSiteMap)

    b.addUrl("index.html")
    b.addUrl("test1.html")
    b.addUrl("test2.html")
    b.addUrl("test3.html")
    b.addUrl("producties.html")

    val siteMap: Seq[SiteMapUrl] = b.build()

    siteMap.size should equal(5)

    siteMap(0) should equal(SiteMapUrl("index.html", "2012-08-11", "monthly", "1.0"))
    siteMap(1) should equal(SiteMapUrl("test1.html", "2012-08-11", "never", "0.5"))
    siteMap(2) should equal(SiteMapUrl("test2.html", "2012-01-01", "never", "0.5"))
    siteMap(3) should equal(SiteMapUrl("test3.html", "2012-08-11", "never", "0.5"))
    siteMap(4) should equal(SiteMapUrl("producties.html", "2012-01-01", "yearly", "0.8"))
  }

  Day.clear()
}

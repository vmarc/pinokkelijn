package web.server.sitemap

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.io.PrintWriter
import java.io.StringWriter

class SiteMapWriterTest extends AnyFunSuite with Matchers {

  test("write sitemap xml") {

    val siteMap = List(
      SiteMapUrl("index.html", "2012-09-15", "monthly", "1.0"),
      SiteMapUrl("test.html", "2012-09-10", "yearly", "0.5")
    )

    val sw = new StringWriter()
    val pw = new PrintWriter(sw)
    new SiteMapWriter(pw).write(siteMap)
    pw.close()
    val xml = sw.toString

    println(xml) // TODO remove after first test

    xml should include("<loc>http://www.pinokkelijn.be/index.html</loc>")
    xml should include("<lastmod>2012-09-15</lastmod>")
    xml should include("<changefreq>monthly</changefreq>")
    xml should include("<priority>1.0</priority>")
  }
}

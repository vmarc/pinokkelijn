package web.server.sitemap

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import java.io.StringReader

class SiteMapReaderTest extends AnyFunSuite with Matchers {

  test("read site map xml") {
//      <?xml version="1.0" encoding="UTF-8"?>
    val xml = """
      <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
         <url>
            <loc>http://www.pinokkelijn.be/index.html</loc>
            <lastmod>2012-01-01</lastmod>
            <changefreq>monthly</changefreq>
            <priority>1.0</priority>
         </url>
         <url>
            <loc>http://www.pinokkelijn.be/test/test.html</loc>
            <lastmod>2012-08-11</lastmod>
            <changefreq>never</changefreq>
            <priority>0.5</priority>
         </url>
      </urlset>
    """

    val sitemap = new SiteMapReader(new StringReader(xml)).read()

    sitemap.size should equal(2)
    sitemap("index.html") should equal (SiteMapUrl("index.html", "2012-01-01", "monthly", "1.0"))
    sitemap("test/test.html") should equal (SiteMapUrl("test/test.html", "2012-08-11", "never", "0.5"))
  }
}

package web.server.sitemap

import java.io.Reader
import scala.xml.XML

class SiteMapReader(reader: Reader) {

  def read(): Map[String, SiteMapUrl] = {
    val urlset = XML.load(reader)
    (urlset \ "url").map { url =>
      val location = (url \ "loc").text.substring("http://www.pinokkelijn.be/".length)
      val lastModified = (url \ "lastmod").text
      val changeFrequency = (url \ "changefreq").text
      val priority = (url \ "priority").text
      (location, SiteMapUrl(location, lastModified, changeFrequency, priority))
    }.toMap
  }
}

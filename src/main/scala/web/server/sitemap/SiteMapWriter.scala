package web.server.sitemap

import java.io.PrintWriter

class SiteMapWriter(out: PrintWriter) {

  def write(urls: Seq[SiteMapUrl]): Unit = {
    //    out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
    out.println("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">")

    urls.foreach { url =>
      out.printf("  <url>\n")
      out.printf("    <loc>http://www.pinokkelijn.be/%s</loc>\n", url.location)
      out.printf("    <lastmod>%s</lastmod>\n", url.lastModified)
      out.printf("    <changefreq>%s</changefreq>\n", url.changeFrequency)
      out.printf("    <priority>%s</priority>\n", url.priority)
      out.printf("  </url>\n")
    }

    out.println("</urlset>")
  }
}

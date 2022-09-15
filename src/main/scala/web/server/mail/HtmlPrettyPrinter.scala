package web.server.mail

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.w3c.tidy.Tidy

import java.io.StringReader
import java.io.StringWriter

@Component
class HtmlPrettyPrinter {

  private val log = LoggerFactory.getLogger(classOf[HtmlPrettyPrinter])

  def prettyPrint(in: String): String = {
    val tidy: Tidy = createTidy()
    val reader = new StringReader(replaceEscapes(in))
    val writer = new StringWriter()
    tidy.parse(reader, writer)
    if (tidy.getParseErrors > 0 || tidy.getParseWarnings > 0) {
      val message = s"**** Tidy errors: ${tidy.getParseErrors}, warnings: ${tidy.getParseWarnings}\n$in"
      log.error(message)
      throw new IllegalStateException(message)
    }
    writer.toString
  }

  private def createTidy(): Tidy = {
    new Tidy() {
      setIndentContent(true)
      setTidyMark(false)
      setUpperCaseTags(false)
      setWraplen(256)
      setQuiet(false)
      setShowWarnings(true)
      setInputEncoding("UTF-8")
      setTrimEmptyElements(false)
      setXHTML(true)
    }
  }

  private def replaceEscapes(string: String): String = {
    var xml = string

    xml = xml.replaceAll("@", "&")
    xml = xml.replaceAll("&pinokkelijn.be", "@pinokkelijn.be")
    xml = xml.replaceAll("marijke&mtfotografie.be", "marijke@mtfotografie.be")

    //    xml = xml.replaceAll("@amp;", "&amp;")
    //    xml = xml.replaceAll("@lt;", "&lt;")
    //    xml = xml.replaceAll("@gt;", "&gt;")
    //    xml = xml.replaceAll("@quot;", "&quot;")
    //    xml = xml.replaceAll("@apos;", "&apos;")
    //
    //    xml = xml.replaceAll("@#238;", "@#238;")
    //    xml = xml.replaceAll("@#39;", "@#39;")
    //    xml = xml.replaceAll("@agrave;", "@agrave;")
    //    xml = xml.replaceAll("@auml;", "@auml;")
    //    xml = xml.replaceAll("@ccedil;", "@ccedil;")
    //    xml = xml.replaceAll("@eacute;", "@eacute;")
    //    xml = xml.replaceAll("@Eacute;", "@Eacute;")
    //    xml = xml.replaceAll("@ecirc;", "@ecirc;")
    //    xml = xml.replaceAll("@egrave;", "@egrave;")
    //    xml = xml.replaceAll("@EgraveNE;", "@EgraveNE;")
    //    xml = xml.replaceAll("@euml;", "@euml;")
    //    xml = xml.replaceAll("@Euml;", "@Euml;")
    //    xml = xml.replaceAll("@iuml;", "@iuml;")
    //    xml = xml.replaceAll("@nbsp;", "@nbsp;")
    //    xml = xml.replaceAll("@oacute;", "@oacute;")
    //    xml = xml.replaceAll("@ouml;", "@ouml;")
    //    xml = xml.replaceAll("@Ouml;", "@Ouml;")
    //    xml = xml.replaceAll("@quot;", "@quot;")
    //    xml = xml.replaceAll("@uuml;", "@uuml;")

    xml
  }
}

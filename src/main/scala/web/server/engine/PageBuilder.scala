package web.server.engine

import org.fusesource.scalate.DefaultRenderContext
import org.fusesource.scalate.TemplateEngine
import org.springframework.stereotype.Component
import web.server.mail.HtmlPrettyPrinter
import web.view.Page

import java.io.PrintWriter
import java.io.StringWriter

@Component
class PageBuilder(htmlPrettyPrinter: HtmlPrettyPrinter) {

  def build(context: Map[String, Any], templateName: String): String = {

    val engine = new TemplateEngine
    val parentTemplate = engine.load("templates/page.ssp")
    val childTemplate = engine.load(templateName)

    val page = new Page()

    val sw = new StringWriter(10000)
    val out = new PrintWriter(sw)
    val renderContext = new DefaultRenderContext("", engine, out)
    renderContext.attributes.update("page", page)

    context.foreach { case (key, value) => renderContext.attributes.update(key, value) }
    engine.layout(childTemplate, renderContext)

    val childContent = sw.toString

    if (page.name.isEmpty) throw new RuntimeException("Page.name not filled configured in " + templateName)
    if (page.title.isEmpty) throw new RuntimeException("Page.title not filled configured in " + templateName)
    if (page.keyWords.isEmpty) throw new RuntimeException("Page.keyWords not filled configured in " + templateName)
    if (page.description.isEmpty) throw new RuntimeException("Page.description not filled configured in " + templateName)

    val sw2 = new StringWriter(10000)
    val out2 = new PrintWriter(sw2)

    val renderContext2 = new DefaultRenderContext("", engine, out2)
    renderContext2.escapeMarkup = false
    context.foreach { case (key, value) => renderContext2.attributes.update(key, value) }
    renderContext2.attributes.update("page", page)
    renderContext2.attributes.update("content", childContent)

    engine.layout(parentTemplate, renderContext2)

    val outputString = sw2.toString
    val prettyfied = htmlPrettyPrinter.prettyPrint(outputString)
    if (!prettyfied.contains("-//W3C//DTD XHTML 1.0 Strict//EN")) {
      throw new IllegalStateException(" does not contain DOCTYPE \"-//W3C//DTD XHTML 1.0 Strict//EN\" SHOULD FIX THIS")
    }
    prettyfied
  }
}

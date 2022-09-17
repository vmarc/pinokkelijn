package web.server.sitemap

import scala.util.matching.Regex
import scala.collection.mutable.ArrayBuffer
import web.domain.Day

class SiteMapBuilder(updatedUrls: Seq[Regex], oldSiteMap: Map[String, SiteMapUrl]) {

  private val priorityUrls = List("intro.html", "producties.html", "personen-lijst.html").map(_.r)

  private val newSiteMap = ArrayBuffer[SiteMapUrl]()

  private val today = Day.today.yyyymmdd

  def build(): Seq[SiteMapUrl] = newSiteMap.toSeq

  def addUrl(location: String): Unit = {
    val priority = location match {
      case "index.html" => "1.0"
      case x if (isPriorityUrl(x)) => "0.8"
      case _ => "0.5"
    }

    val changeFrequency = location match {
      case "index.html" => "monthly"
      case x if (isPriorityUrl(x)) => "yearly"
      case _ => "never"
    }

    val lastModified = if (isUpdatedUrl(location)) {
      today
    }
    else {
      oldSiteMap.get(location) match {
        case Some(entry: SiteMapUrl) => entry.lastModified
        case None => today
      }
    }
    newSiteMap += SiteMapUrl(location, lastModified, changeFrequency, priority)
  }

  // location matches one of the patterns in 'priorityUrls'
  private def isPriorityUrl(url: String): Boolean = {
    matching(url, priorityUrls)
  }

  // url matches one of the patterns in 'updatedUrls'
  private def isUpdatedUrl(url: String): Boolean = {
    matching(url, updatedUrls)
  }

  private def matching(value: String, regexes: Seq[Regex]): Boolean = {
    regexes.exists(regex => regex.pattern.matcher(value).matches)
  }
}

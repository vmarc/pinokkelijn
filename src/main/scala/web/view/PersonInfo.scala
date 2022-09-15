package web.view

import web.domain.Person
import web.domain.PersonDetail

case class PersonInfo(person: Person, previous: Option[Person], next: Option[Person], details: Seq[PersonDetail]) {

  def statistics: String = List(productionStatistics, photoStatistics).filter(_.nonEmpty).mkString(", ")
  
  private def productionStatistics: String = {
    if (details.size < 2) "" else "%d producties".format(details.size) 
  }
  private def photoStatistics: String = if (photoCount < 2) "" else "%d fotos".format(photoCount) 
  private def photoCount: Int = details.map(_.photos.size).sum
  
  def longStatistics: String = List(longProductionStatistics, longPhotoStatistics).filter(_.nonEmpty).mkString(", ")
  
  private def longProductionStatistics: String = {
    details.size match {
      case 0 => ""
      case 1 => "1 productie"
      case x => "%d producties".format(x) 
    }
  }

  private def longPhotoStatistics: String = {
    photoCount match {
      case 0 => ""
      case 1 => "1 foto"
      case x => "%d fotos".format(x) 
    }
  }
}

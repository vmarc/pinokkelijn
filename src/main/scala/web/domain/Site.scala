package web.domain

case class Site(productions: Seq[Production], persons: Seq[Person]) {

  def letterPersonsCollection: Seq[LetterPersons] = {
    persons.groupBy(_.letter).toList.map{ case(letter, p) => 
      LetterPersons(0, letter, p.sortWith(_.fullName < _.fullName))
    }.sortWith(_.letter < _.letter)
  }
  
  def productionPhotos(web: Boolean): Seq[Photo] =  {
    productions.flatMap { production =>
      if (web) production.photos.filter(_.web) else production.photos
    }
  }
}

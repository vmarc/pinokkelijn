package web.domain

case class Site(productions: Seq[Production], persons: Seq[Person]) {

  def letterPersonsCollection: Seq[LetterPersons] = {
    val letterPersonsCollection = persons.groupBy(_.letter).toList.map{ case(letter, p) =>
      LetterPersons(0, letter, p.sortWith(_.fullName < _.fullName))
    }.sortWith(_.letter < _.letter)

    var personIndex = -1
    letterPersonsCollection.map { letterPersons =>
      val indexedPersons = letterPersons.persons.map { person =>
        personIndex = personIndex + 1
        person.copy(
          index = personIndex
        )
      }
      letterPersons.copy(persons = indexedPersons)
    }
  }
  
  def productionPhotos(web: Boolean): Seq[Photo] =  {
    productions.flatMap { production =>
      if (web) production.photos.filter(_.web) else production.photos
    }
  }
}

package web.domain

case class Site(productions: Seq[Production], persons: Seq[Person]) {

  def letterPersonsCollection: Seq[LetterPersons] = {
    var personIndex = -1
    persons.groupBy(_.letter).toSeq.sortBy(_._1).map { case (letter, personsGroup) =>
      val letterPersons = personsGroup.map { person =>
        val productionCount = productions.count(production => production.persons.map(_.key).contains(person.key))
        val photoCount = productions.map(_.photos.filter(_.web).filter(_.persons.map(_.key).contains(person.key)).size).sum
        val s1 = if (productionCount == 1) {
          "1 productie"
        }
        else {
          s"$productionCount producties"
        }
        val s2 = if (photoCount == 0) {
          ""
        }
        else if (photoCount == 1) {
          ", 1 foto"
        }
        else {
          s", $photoCount fotos"
        }
        val statistics = s"($s1$s2)"

        personIndex = personIndex + 1
        LetterPerson(
          personIndex,
          person.key,
          person.fullName,
          statistics
        )
      }
      LetterPersons(letter, letterPersons)
    }
  }
  
  def productionPhotos(web: Boolean): Seq[Photo] =  {
    productions.flatMap { production =>
      if (web) production.photos.filter(_.web) else production.photos
    }
  }
}

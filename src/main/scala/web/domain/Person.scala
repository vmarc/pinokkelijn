package web.domain

object Person {
  def compareByPopularity(person1: Person, person2: Person): Boolean = {
    if (person1.details.head.production.id > person2.details.head.production.id) {
      true
    }
    else if (person1.details.head.production.id < person2.details.head.production.id) {
      false
    }
    else {
      if (person1.details.size > person2.details.size) {
        true
      }
      else if (person1.details.size < person2.details.size) {
        false
      }
      else {
        person1.fullName < person2.fullName
      }
    }
  }

  def compareByName(person1: Person, person2: Person): Boolean = {
    if (person1.lastName == person2.lastName) {
      person1.firstName < person2.firstName
    }
    else {
      person1.lastName < person2.lastName
    }
  }
}

case class Person(index: Int, key: String, lastName: String, firstName: String, details: Seq[PersonDetail]) {

  def letter: Char = lastName.toUpperCase.head

  def name: String = firstName + " " + lastName

  def photos: Seq[Photo] = details.flatMap(_.photos)

  def fullName: String = {
    s"$lastName, $firstName"
  }

  def webDetails: Seq[PersonDetail] = details.map(_.webDetail)

}

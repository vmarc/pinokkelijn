package web.domain

case class Person(key: String, lastName: String, firstName: String, details: Seq[PersonDetail]) {

  def letter: Char = lastName.toUpperCase.head

  def name: String = firstName + " " + lastName

  def photos: Seq[Photo] = details.flatMap(_.photos)

  def fullName: String = {
    s"$lastName, $firstName"
  }

  def webDetails: Seq[PersonDetail] = details.map(_.webDetail)

}

package web.domain

case class Production(
  id: String,
  title: String,
  year: String,
  shortDescription: String,
  description: String,
  performances: Seq[Performance],
  location: String = "",
  actors: Seq[Actor],
  articles: Seq[String],
  tasks: Seq[Task],
  photos: Seq[Photo],
) {
  def longTitle: String = year + " - " + title

  def persons: Seq[Person] = (personsInActors ++ personsInTasks ++ personsInPhotos).toSet.toList

  def photos(person: Person): Seq[Photo] = photos.filter(_.persons.contains(person))

  def contributions(person: Person): Seq[String] = roles(person) ++ tasks(person)

  private def roles(person: Person): Seq[String] = actors.filter(_.person eq person).map(_.longRole)

  private def tasks(person: Person): Seq[String] = tasks.filter(_.persons.contains(person)).map(_.description)

  private def personsInActors: Seq[Person] = actors.map(_.person).filter(_.key.nonEmpty)

  private def personsInTasks: Seq[Person] = tasks.flatMap(_.persons).filter(_.key.nonEmpty)

  private def personsInPhotos: Seq[Person] = photos.flatMap(_.persons).filter(_.key.nonEmpty)
}

package web.pages

case class PersonsPage(
  root: String,
  letterPersonsCollection: Seq[web.domain.LetterPersons]
) extends Page {

  def name: String = "persons"

  def title: String = "Medewerkers"

  def keyWords: String = "medewerkers"

  def description: String = "Lijst met Pinokkelijn medewerkers doorheen de jaren, met fotos en overzicht van producties."

}

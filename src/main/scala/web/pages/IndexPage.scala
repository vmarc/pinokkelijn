package web.pages

case class IndexPage(
  root: String
) extends Page {

  def name: String = "home"

  def title: String = "Home"

  def keyWords: String = "home"

  def description: String = "Toneel vereniging Pinokkelijn uit Essen, Antwerpen: producties, spelers en medewerkers; links naar andere verenigingen in Vlaanderen."

}

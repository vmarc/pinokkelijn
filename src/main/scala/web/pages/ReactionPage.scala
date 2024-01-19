package web.pages

case class ReactionPage(
  root: String
) extends Page {

  def name: String = "none"

  def title: String = "Reactie"

  def keyWords: String = "reactie contact reageren commentaar feedback"

  def description: String = "Web formuliertje voor het achterlaten van vragen of commentaar"

}

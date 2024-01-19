package web.pages

case class ReactionOkPage(
  root: String,
  reaction: web.server.mail.domain.Reaction
) extends Page {

  def name: String = "none"

  def title: String = "Reactie bevestiging"

  def keyWords: String = "keyWords"

  def description: String = "descriptions"

}

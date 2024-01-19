package web.pages

case class IntroPage(
  root: String
) extends Page {

  def name: String = "intro"

  def title: String = "Intro"

  def keyWords: String = "intro geschiedenis onstaan"

  def description: String = "Een korte voorstelling van toneelvereniging Pinokkelijn uit Essen"

}

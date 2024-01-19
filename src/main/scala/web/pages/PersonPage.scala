package web.pages

case class PersonPage(
  root: String,
  info: web.view.PersonInfo,
  images: web.view.Images
) extends Page {

  def name: String = "none"

  def title: String = info.person.name

  def keyWords: String = info.person.name

  def description: String = "%s - Pinokkelijn - %s".format(info.person.name, info.longStatistics)

}

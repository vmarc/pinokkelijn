package web.pages

case class PostersPage(
  root: String,
  productions: Seq[web.domain.Production],
  images: web.view.Images
) extends Page {

  def name: String = "posters"

  def title: String = "Affiches"

  def keyWords: String = "theater affiches theatre posters"

  def description: String = "Overzicht met Pinokkelijn affiches door de jaren heen"

}

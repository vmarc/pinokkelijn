package web.pages

case class ProductionPage(
  root: String,
  production: web.domain.Production,
  previousProduction: Option[web.domain.Production],
  nextProduction: Option[web.domain.Production],
  photos: Seq[web.domain.Photo],
  images: web.view.Images
) extends Page {

  def name: String = "none"

  def title: String = production.title

  def keyWords: String = production.longTitle

  def description: String = "Pinokkelijn theater productie: %s".format(production.longTitle)

}

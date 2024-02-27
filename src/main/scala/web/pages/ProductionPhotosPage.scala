package web.pages

case class ProductionPhotosPage(
  root: String,
  production: web.domain.Production,
  previousProduction: Option[web.domain.Production],
  nextProduction: Option[web.domain.Production],
  photos: Seq[web.domain.Photo],
  images: web.view.Images
) extends Page {

  def name: String = "none"

  def title: String = production.title + " fotos"

  def keyWords: String = production.longTitle

  def description: String = "Pinokkelijn productie fotos: %s".format(production.longTitle)

  override def containsGallery: Boolean = true

}

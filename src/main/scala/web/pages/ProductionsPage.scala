package web.pages

case class ProductionsPage(
  root: String,
  productions: Seq[web.domain.Production]
) extends Page {

  def name: String = "productions"

  def title: String = "Producties"

  def keyWords: String = "producties"

  def description: String = "Lijst van voorbije producties, informatie en fotos van de voorstellingen."

}

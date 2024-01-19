package web.pages

case class FacesPage(
  personInfos: Seq[web.view.PersonInfo],
  images: web.view.Images,
  root: String
) extends Page {

  def name: String = "faces"

  def title: String = "Medewerkers"

  def keyWords: String = "medewerkers"

  def description: String = "Lijst met Pinokkelijn medewerkers doorheen de jaren, met fotos en overzicht van producties."

}

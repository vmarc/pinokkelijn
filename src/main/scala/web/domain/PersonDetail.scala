package web.domain

/**
 * Instances of this class represent a given person's contribution(s) and images
 * for a given production.
 */
case class PersonDetail(
  production: Production,
  photos: Seq[Photo],
  contributions: Seq[String]
) {

  def webDetail: PersonDetail = {
    val webPhotos = photos.filter(_.web)
    PersonDetail(production, webPhotos, contributions)
  }

  def contributionsString: String = {
    val strings = contributions.map(_.trim).filter(_.nonEmpty)
    strings.size match {
      case 0 => ""
      case 1 => "(Rol: %s)".format(strings.head)
      case _ => "(Rollen: %s)".format(strings.mkString(" + "))
    }
  }
}

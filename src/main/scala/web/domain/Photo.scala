package web.domain

case class Photo(
  id: String,
  description: String,
  photographer: String,
  web: Boolean,
  show: Boolean,
  persons: Seq[Person],
  productionId: String
) {

  def productionTitleText: String = {
    "Pinokkelijn " + "TODO pick up production name based on production id" // production.longTitle + productionPersonsText2
  }

  def productionCaptionText: String = {
    descriptionText + productionPersonsText
  }

  def personCaptionText: String = {
    descriptionText + personPersonsText
  }

  def personTitleText: String = productionPersonsText2

  def altText: String = {
    descriptionText + personsText

    // TODO link naar persoon zelf hoeft niet
    // TODO kan wel rol/taak toevoegen aan naam van persoon op de foto


    /** *
     * #if (current.photographer.nonEmpty)
     * <tr  align='right' class='photographer'>
     * <td align='right' width='*'>
     * <%--                  [foto: ${current.photographer}] --%>
     * </td>
     * </tr>
     * #end
     * * */

  }

  private def descriptionText: String = {
    if (description.isEmpty) {
      ""
    }
    else {
      description + "\n"
    }
  }

  private def personsText: String = {

    if (persons.isEmpty) {
      ""
    }
    else {
      val prefix = if (persons.size > 1) "vlnr: " else ""
      val links = persons.map { person =>
        if (person.key.isEmpty) {
          person.name
        }
        else {
          "<a href='../../personen/%s.html'>%s</a>".format(person.key, person.name)
        }
      }.mkString("&nbsp;|&nbsp;")

      prefix + links
    }
  }

  private def productionPersonsText: String = {

    if (persons.isEmpty) {
      ""
    }
    else {
      val prefix = if (persons.size > 1) "vlnr: " else ""
      val links = persons.map { person =>
        if (person.key.isEmpty) {
          person.name
        }
        else {
          "<a href='../../personen/%s.html'>%s</a>".format(person.key, person.name)
        }
      }.mkString("&nbsp;|&nbsp;")

      prefix + links
    }
  }

  private def productionPersonsText2: String = {
    persons.map(_.name).mkString(" - ", ", ", "")
    //    if (persons isEmpty) { // TODO remove if above statement works ok
    //      ""
    //    }
    //    else {
    //      " - " + persons.map(_.name).mkString(", ")
    //    }
  }

  private def personPersonsText: String = {

    if (persons.isEmpty) {
      ""
    }
    else {
      val prefix = if (persons.size > 1) "vlnr: " else ""
      val links = persons.map { person =>
        if (person.key.isEmpty) {
          person.name
        }
        else {
          "<a href='%s.html'>%s</a>".format(person.key, person.name) // <== this is what is different with previous function
        }
      }.mkString("&nbsp;|&nbsp;")

      prefix + links
    }
  }
}

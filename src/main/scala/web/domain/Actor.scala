package web.domain

case class Actor(person: Person, role: String, description: String) {
  def longRole: String = {
    if (description.isEmpty) {
      role
    } else {
      role + ", " + description
    }
  }
}

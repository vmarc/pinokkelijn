package web.server.mail.domain

case class Reaction(
  commentaar: String,
  uitnodigingEmail: String,
  email: String,
  naam: String,
  adres1: String,
  adres2: String,
)

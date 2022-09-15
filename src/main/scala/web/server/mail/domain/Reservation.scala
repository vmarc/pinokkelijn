package web.server.mail.domain

case class Reservation(
  aantal: String,
  aantalMin12: String,
  datum: String,
  naam: String,
  email: String,
  adres1: String,
  adres2: String,
  opDeHoogteBlijven: String,
  commentaar: String,
  codewoord: String,
)

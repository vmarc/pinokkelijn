package web.server.mail

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import web.server.mail.domain.Reservation

@Component
class ReservationMailSender(
  mailSender: JavaMailSender,
  mailFrom: String,
  reservationMailTo: String
) {

  def send(reservation: Reservation): Unit = {
    val tos = reservationMailTo.split(",").toSeq
    val message = new SimpleMailMessage
    message.setFrom(mailFrom)
    message.setReplyTo(mailFrom)
    message.setTo(tos: _*)
    message.setSubject("Reservatie")
    message.setText(mailText(reservation))
    mailSender.send(message)
  }

  private def mailText(reservation: Reservation): String = {
    s"""
       |aantal: ${reservation.aantal}
       |datum: ${reservation.datum}
       |naam: ${reservation.naam}
       |email: ${reservation.email}
       |adres1: ${reservation.adres1}
       |adres2: ${reservation.adres2}
       |opDeHoogteBlijven: ${reservation.opDeHoogteBlijven}
       |commentaar: ${reservation.commentaar.trim}
    """.stripMargin
  }
}

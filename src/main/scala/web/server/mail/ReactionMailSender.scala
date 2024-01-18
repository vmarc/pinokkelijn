package web.server.mail

import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Component
import web.server.mail.domain.Reaction

@Component
class ReactionMailSender(
  mailSender: JavaMailSender,
  mailFrom: String,
  reactionMailTo: String
) {

  def send(reaction: Reaction): Unit = {
    val tos: Seq[String] = reactionMailTo.split(",").toSeq
    val message = new SimpleMailMessage
    message.setFrom(mailFrom)
    message.setReplyTo(mailFrom)
    message.setTo(tos: _*)
    message.setSubject("Reactie")
    message.setText(mailText(reaction))
    mailSender.send(message)
  }

  private def mailText(reaction: Reaction): String = {
    s"""
       |commentaar: ${reaction.commentaar}
       |uitnodigingEmail: ${reaction.uitnodigingEmail}
       |email: ${reaction.email}
       |naam: ${reaction.naam}
       |adres1: ${reaction.adres1}
       |adres2: ${reaction.adres2}
    """.stripMargin
  }
}

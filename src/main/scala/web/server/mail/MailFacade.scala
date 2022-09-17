package web.server.mail

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import web.domain.ImageDimension
import web.server.engine.PageBuilder
import web.server.mail.domain.Reaction
import web.server.mail.domain.Reservation
import web.tools.SiteBuilderOptions
import web.view.Images

@Component
class MailFacade(
  reactionMailSender: ReactionMailSender,
  reservationMailSender: ReservationMailSender,
  pageGenerator: PageBuilder
) {

  private val log = LoggerFactory.getLogger(classOf[MailController])

  def reservationNok(reservation: Reservation): String = {
    val images: Images = new Images(
      SiteBuilderOptions(),
      Seq(
        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
      ).toMap
    )
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "reservation" -> reservation
    )
    pageGenerator.build(context, "templates/reservation-nok.ssp")
  }

  def reaction(reaction: Reaction): String = {
    log.info(s"reaction: $reaction")
    reactionMailSender.send(reaction)
    reactionOk(reaction)
  }

  def reservation(reservation: Reservation): String = {
    log.info(s"reservation: $reservation")
    reservationMailSender.send(reservation)
    if ("revue".equals(reservation.codewoord.toLowerCase())) {
      reservationOk(reservation)
    }
    else {
      reservationNok(reservation)
    }
  }

  def reactionOk(reaction: Reaction): String = {
    val images: Images = new Images(
      SiteBuilderOptions(),
      Seq(
        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
      ).toMap
    )
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "reaction" -> reaction
    )
    pageGenerator.build(context, "templates/reaction-ok.ssp")
  }

  def reservationOk(reservation: Reservation): String = {
    val images: Images = new Images(
      SiteBuilderOptions(),
      Seq(
        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
      ).toMap
    )
    val context = Map[String, Any](
      "root" -> ".",
      "images" -> images,
      "reservation" -> reservation
    )
    pageGenerator.build(context, "templates/reservation-ok.ssp")
  }
}

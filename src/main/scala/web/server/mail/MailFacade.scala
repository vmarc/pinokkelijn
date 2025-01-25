package web.server.mail

import org.jsoup.Jsoup
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import play.twirl.api.Html
import play.twirl.api.Template1
import play.twirl.api.Template3
import web.common.Templates
import web.domain.ImageDimension
import web.pages.Page
import web.pages.ReactionOkPage
import web.pages.ReservationNokPage
import web.pages.ReservationOkPage
import web.server.mail.domain.Reaction
import web.server.mail.domain.Reservation
import web.tools.SiteBuilderOptions
import web.view.Images

@Component
class MailFacade(
  reactionMailSender: ReactionMailSender,
  reservationMailSender: ReservationMailSender
) {

  private val log = LoggerFactory.getLogger(classOf[MailController])

  def reaction(reaction: Reaction): String = {
    log.info(s"reaction: $reaction")
    reactionMailSender.send(reaction)
    reactionOk(reaction)
  }

  def reservation(reservation: Reservation): String = {
    log.info(s"reservation: $reservation")
    reservationMailSender.send(reservation)
    if ("dwazen".equals(reservation.codewoord.toLowerCase())) {
      reservationOk(reservation)
    }
    else {
      reservationNok(reservation)
    }
  }

  def reactionOk(reaction: Reaction): String = {
    val page = ReactionOkPage(".", reaction)
    val contentTemplate = Templates.getTemplate[Template1[ReactionOkPage, Html]]("html.reactionOk")
    val contents = contentTemplate.render(page)
    renderPage(page, contents)
  }

  def reservationOk(reservation: Reservation): String = {
    val page = ReservationOkPage(".")
    val contentTemplate = Templates.getTemplate[Template1[ReservationOkPage, Html]]("html.reservationOk")
    val contents = contentTemplate.render(page)
    renderPage(page, contents)
  }

  def reservationNok(reservation: Reservation): String = {
    val page = ReservationNokPage(".")
    val contentTemplate = Templates.getTemplate[Template1[ReservationNokPage, Html]]("html.reservationNok")
    val contents = contentTemplate.render(page)
    renderPage(page, contents)
  }

  private def renderPage(page: Page, contents: Html): String = {
    val pageTemplate = Templates.getTemplate[Template3[web.pages.Page, Images, Html, Html]]("html.page")
    val string = pageTemplate.render(page, images(), contents)
    Jsoup.parse(string.toString()).html()
  }

  private def images(): Images = {
    new Images(
      SiteBuilderOptions(),
      Seq(
        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
      ).toMap
    )
  }
}

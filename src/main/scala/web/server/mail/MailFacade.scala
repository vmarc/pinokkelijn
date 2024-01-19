package web.server.mail

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import web.server.mail.domain.Reaction
import web.server.mail.domain.Reservation

@Component
class MailFacade(
  reactionMailSender: ReactionMailSender,
  reservationMailSender: ReservationMailSender
) {

  private val log = LoggerFactory.getLogger(classOf[MailController])

  def reservationNok(reservation: Reservation): String = {
    """|<html>
       |<body>
       |<p>
       |  FOUT: Uw reservatie is niet gelukt.
       |</p>
       |<p>
       |  Ga terug naar de vorige pagina en zorg dat het codewoord juist ingevuld is.
       |</p>
       |<p>
       |  <a href="javascript:history.back()">Terug</a>
       |</p>
       |</body>
       |</html>
    """.stripMargin

//    val images: Images = new Images(
//      SiteBuilderOptions(),
//      Seq(
//        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
//      ).toMap
//    )
//    val context = Map[String, Any](
//      "root" -> ".",
//      "images" -> images,
//      "reservation" -> reservation
//    )
//    pageGenerator.build(context, "templates/reservation-nok.ssp")
  }

  def reaction(reaction: Reaction): String = {
    log.info(s"reaction: $reaction")
    reactionMailSender.send(reaction)
    reactionOk(reaction)
  }

  def reservation(reservation: Reservation): String = {
    log.info(s"reservation: $reservation")
    reservationMailSender.send(reservation)
    if ("flarden".equals(reservation.codewoord.toLowerCase())) {
      reservationOk(reservation)
    }
    else {
      reservationNok(reservation)
    }
  }

  def reactionOk(reaction: Reaction): String = {
    """|<html>
       |<body>
       |<p>Bedankt voor uw reactie!!!</p>
       |</body>
       |</html>
    """.stripMargin

//    val images: Images = new Images(
//      SiteBuilderOptions(),
//      Seq(
//        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
//      ).toMap
//    )
//    val context = Map[String, Any](
//      "root" -> ".",
//      "images" -> images,
//      "reaction" -> reaction
//    )
//    pageGenerator.build(context, "templates/reaction-ok.ssp")
  }

  def reservationOk(reservation: Reservation): String = {
    """|<html>
       |<body>
       |<p>
       |  Uw reservatie is verstuurd.
       |</p>
       |<p>
       |  De kaarten zijn aan de kassa ter beschikking op de avond van de voorstelling en kunnen daar betaald worden.
       |</p>
       |</body>
       |</html>
    """.stripMargin

    //    val images: Images = new Images(
    //      SiteBuilderOptions(),
    //      Seq(
    //        "images/pinokkelijn4.jpg" -> ImageDimension("302", "50")
    //      ).toMap
    //    )
    //    val context = Map[String, Any](
    //      "root" -> ".",
    //      "images" -> images,
    //      "reservation" -> reservation
    //    )
    //    pageGenerator.build(context, "templates/reservation-ok.ssp")
  }
}

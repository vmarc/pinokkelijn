package web.server.mail

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import web.server.mail.domain.Reaction
import web.server.mail.domain.Reservation

@RestController
@RequestMapping(Array("/"))
class MailController(mailFacade: MailFacade) {

  @PostMapping(value = Array("reactionconfirmation"))
  def reaction(
    @RequestParam commentaar: String,
    @RequestParam uitnodigingEmail: String,
    @RequestParam email: String,
    @RequestParam naam: String,
    @RequestParam adres1: String,
    @RequestParam adres2: String
  ): String = {
    mailFacade.reaction(
      Reaction(
        commentaar,
        uitnodigingEmail,
        email,
        naam,
        adres1,
        adres2
      )
    )
  }

  @PostMapping(value = Array("reservationconfirmation"))
  def reservation(
    @RequestParam aantal: String,
    @RequestParam aantalMin12: String,
    @RequestParam datum: String,
    @RequestParam naam: String,
    @RequestParam email: String,
    @RequestParam adres1: String,
    @RequestParam adres2: String,
    @RequestParam opDeHoogteBlijven: String,
    @RequestParam commentaar: String,
    @RequestParam codewoord: String,
  ): String = {
    mailFacade.reservation(
      Reservation(
        aantal,
        aantalMin12,
        datum,
        naam,
        email,
        adres1,
        adres2,
        opDeHoogteBlijven,
        commentaar,
        codewoord
      )
    )
  }

  @GetMapping(value = Array("test-reaction-ok"))
  def testReactionOk(): String = {
    mailFacade.reactionOk(
      Reaction(
        "*commentaar*",
        "*uitnodigingEmail*",
        "*email*",
        "*naam*",
        "*adres1*",
        "*adres2*"
      )
    )
  }

  @GetMapping(value = Array("test-reservation-ok"))
  def testReservationOk(): String = {
    mailFacade.reservationOk(
      Reservation(
        "aantal",
        "aantalMin12",
        "datum",
        "naam",
        "email",
        "adres1",
        "adres2",
        "opDeHoogteBlijven",
        "commentaar",
        "codewoord"
      )
    )
  }

  @GetMapping(value = Array("test-reservation-nok"))
  def testReservationNok(): String = {
    mailFacade.reservationNok(
      Reservation(
        "aantal",
        "aantalMin12",
        "datum",
        "naam",
        "email",
        "adres1",
        "adres2",
        "opDeHoogteBlijven",
        "commentaar",
        "codewoord"
      )
    )
  }
}

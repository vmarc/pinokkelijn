package web.server

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServerConfiguration {

  @Bean
  def mailFrom(@Value("${mail.from}") value: String): String = {
    value
  }

  @Bean
  def reactionMailTo(@Value("${mail.reaction-mail-to}") value: String): String = {
    value
  }

  @Bean
  def reservationMailTo(@Value("${mail.reservation-mail-to}") value: String): String = {
    value
  }
}

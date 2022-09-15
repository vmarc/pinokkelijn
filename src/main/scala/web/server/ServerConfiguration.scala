package web.server

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl

import java.io.File
import java.io.FileReader
import java.util.Properties

@Configuration
class ServerConfiguration {
  @Bean
  def mailSender: JavaMailSender = {
    val javaMailSender = new JavaMailSenderImpl()


    val properties = new Properties()
    properties.load(new FileReader(new File("/kpn/conf/server-mail.properties")))
    javaMailSender.setHost(properties.getProperty("spring.mail.host"))
    javaMailSender.setPort(properties.getProperty("spring.mail.port").toInt)
    javaMailSender.setUsername(properties.getProperty("spring.mail.username"))
    javaMailSender.setPassword(properties.getProperty("spring.mail.password"))

    val props = javaMailSender.getJavaMailProperties
    props.put("mail.smtp.auth", properties.getProperty("spring.mail.properties.mail.smtp.auth"))
    props.put("mail.smtp.starttls.enable", properties.getProperty("spring.mail.properties.mail.smtp.starttls.enable"))
    props.put("mail.debug", properties.getProperty("spring.mail.properties.mail.debug"))

    javaMailSender
  }

  @Bean
  def mailFrom(@Value("mail.from") value: String): String = {
    value
  }

  @Bean
  def reactionMailTo(@Value("mail.reaction-mail-to") value: String): String = {
    value
  }

  @Bean
  def reservationMailTo(@Value("mail.reservation-mail-to") value: String): String = {
    value
  }
}

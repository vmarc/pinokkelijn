package web.server

import org.springframework.boot.SpringApplication

object Pinokkelijn {
  def main(args: Array[String]): Unit = {
    val app: Array[Class[_]] = Array(classOf[ServerApplication])
    SpringApplication.run(app, args)
  }
}

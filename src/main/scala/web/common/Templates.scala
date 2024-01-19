package web.common

object Templates {
  def getTemplate[T](name: String)(implicit man: Manifest[T]): T = {
    Class.forName(name + "$").getField("MODULE$").get(man.runtimeClass).asInstanceOf[T]
  }
}

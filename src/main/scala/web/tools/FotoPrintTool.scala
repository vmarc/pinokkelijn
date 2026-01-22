package web.tools

object FotoPrintTool {

  def main(args: Array[String]): Unit = {
    val fotoElements = 190.to(311).map(id => String.format("%03d", id)).map { id =>
      s"""
         |
         |<foto id="2025-$id" web="false" fotograaf="Valerie Dufraing">
         |  <personen>
         |    <naam>Plien Kets</naam>
         |    <naam>Jan Meeusen</naam>
         |    <naam>Carine Bergmans</naam>
         |    <naam>Herman Dufraing</naam>
         |    <naam>Tineke Pockelé</naam>
         |    <naam>Noëlla De Rop</naam>
         |    <naam>Gilberte Verbeeck</naam>
         |    <naam>Kristin Verbeeck</naam>
         |    <naam>Jef Baetens</naam>
         |    <naam>Kevin Van der Sommen</naam>
         |    <naam>Bart Van Loenhout</naam>
         |  </personen>
         |</foto>
         """.stripMargin
    }
    fotoElements.foreach(println)
  }
}

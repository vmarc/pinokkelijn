package web.tools

object FotoPrintTool {

  def main(args: Array[String]): Unit = {
    val fotoElements = 1.to(172).map(id => String.format("%03d", id)).map { id =>
      s"""
         |<foto id="2017-$id" web="false" fotograaf="Marijke Bisschops">
         |  <personen>
         |    <naam>Michiel Peeters</naam>
         |    <naam>Gilberte Verbeeck</naam>
         |    <naam>Kristin Verbeeck</naam>
         |    <naam>Mitte Scheldeman</naam>
         |    <naam>Jarrick De Kock</naam>
         |    <naam>Luc Brosens</naam>
         |    <naam>Herman Dufraing</naam>
         |    <naam>Jan Meeusen</naam>
         |    <naam>Chris De Rijck</naam>
         |  </personen>
         |</foto>
         """.stripMargin
    }
    fotoElements.foreach(println)
  }
}

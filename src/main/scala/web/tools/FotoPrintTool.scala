package web.tools

object FotoPrintTool {

  def main(args: Array[String]): Unit = {
    val fotoElements = 1.to(309).map(id => String.format("%03d", id)).map { id =>
      s"""
         |<foto id="2013-$id" web="true" fotograaf="Marijke Bisschops">
         |  <personen>
         |    <naam>Jan Meeusen</naam>
         |    <naam>Tess Aernouts</naam>
         |    <naam>Herman Dufraing</naam>
         |    <naam>Philippe Damen</naam>
         |    <naam>Herman Dufraing</naam>
         |    <naam>Sanne Clous</naam>
         |    <naam>Ren@eacute; Fret</naam>
         |  </personen>
         |</foto>
         """.stripMargin
    }
    fotoElements.foreach(println)
  }
}

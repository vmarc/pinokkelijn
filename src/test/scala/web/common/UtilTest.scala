package web.common

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import web.common.Util.slide
import web.view.Triplet

class UtilTest extends AnyFunSuite with Matchers {

  test("slide collection") {
    slide(Seq("A", "B", "C", "D")) should equal(
      Seq(
        Triplet(None, "A", Some("B")),
        Triplet(Some("A"), "B", Some("C")),
        Triplet(Some("B"), "C", Some("D")),
        Triplet(Some("C"), "D", None),
      )
    )
  }

  test("slide 1 element collection") {
    slide(Seq("A")) should equal(
      Seq(
        Triplet(None, "A", None),
      )
    )
  }

  test("slide empty collection") {
    val emptyCollection: Seq[String] = Seq.empty
    slide(emptyCollection) should equal(Seq.empty)
  }
}

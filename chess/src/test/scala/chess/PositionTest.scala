package chess

import org.scalatest.{Matchers, FunSuite}

/**
  * Created by null on 17.01.16.
  */
class PositionTest extends FunSuite with Matchers {

  test("testCompare") {
    val pos1 = Position(0,0)
    val pos2 = Position(0,1)
    val pos3 = Position(1,1)

    pos1.compare(pos1) shouldBe 0
    pos1.compare(pos2) shouldBe -1
    pos1.compare(pos3) shouldBe -1
    pos3.compare(pos2) shouldBe 1
    pos3.compare(pos1) shouldBe 1
  }
}

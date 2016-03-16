package chess

import org.scalatest.{Matchers, FunSuite}

/**
  * Created by null on 12/26/15.
  */
class KingTest extends FunSuite with Matchers {

  test("testCanAttack") {
    val board = Board(3, 3)
    val king = new King(Position(0, 0))(board)

    king.canAttack(Position(1, 1)) shouldBe true
    king.canAttack(Position(2, 2)) shouldBe false
    an[RuntimeException] should be thrownBy king.canAttack(Position(0, 0))
  }

  test("testPossiblePointsToAttack") {
    val board = Board(3, 3)
    val king = new King(Position(1, 1))(board)
    king.possiblePositionsToAttack should have size 8

    val king2 = new King(Position(0, 0))(board)
    king2.possiblePositionsToAttack should have size 3

    val king3 = new King(Position(2, 2))(board)
    king3.possiblePositionsToAttack should have size 3

    val king4 = new King(Position(0, 1))(board)
    king4.possiblePositionsToAttack should have size 5
  }
}

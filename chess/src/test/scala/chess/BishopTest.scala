package chess

import org.scalatest.{Matchers, FunSuite}

/**
  * Created by null on 18.01.16.
  */
class BishopTest extends FunSuite with Matchers {

  test("testPossiblePositionsToAttack") {
    val board = Board(3, 3)
    val bishop = new Bishop(Position(1, 1))(board)
    bishop.possiblePositionsToAttack should have size 4

    val bishop2 = new Bishop(Position(0, 0))(board)
    bishop2.possiblePositionsToAttack should have size 2

    val bishop3 = new Bishop(Position(2, 2))(board)
    bishop3.possiblePositionsToAttack should have size 2

    val bishop4 = new Bishop(Position(0, 1))(board)
    bishop4.possiblePositionsToAttack should have size 2
  }

}

package chess

import org.scalatest.{Matchers, FunSuite}

/**
  * Created by null on 18.01.16.
  */
class QueenTest extends FunSuite with Matchers {

  test("testPossiblePositionsToAttack") {
    val board = Board(3, 3)
    val queen = new Queen(Position(1, 1))(board)
    println(queen.possiblePositionsToAttack)
    queen.possiblePositionsToAttack should have size 8
  }
}

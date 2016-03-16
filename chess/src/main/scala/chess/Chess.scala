package chess

object Chess extends App {

  val board = Board(6, 6)
  val pieces = List(
    PiecesType.KingType,
    PiecesType.KingType,
    PiecesType.QueenType,
    PiecesType.RookType
    //PiecesType.BishopType,
    //PiecesType.KnightType
  )

  game()
  println("Layers count: " + PiecesOnBoard.size)

  def game() = {
    Game.check(board, pieces)
  }
}
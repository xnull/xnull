package chess

case class Board(width: Int, height: Int) {
  val allPositions: Set[Position] = {
    val positions = for {
      x <- 0 until width
      y <- 0 until height
    } yield {
      PositionFactory.position(x, y)
    }

    positions.toSet
  }

  def isPositionOnBoard(position: Position) = {
    position.x >= 0 && position.y >= 0 && position.x < width && position.y < height
  }
}

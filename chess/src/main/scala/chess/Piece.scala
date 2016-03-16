package chess

import chess.PiecesType._

import scala.collection.immutable.IndexedSeq
import scala.collection.mutable

object PiecesType {

  sealed class PieceType(val pieceName: String) extends Ordered[PieceType] {
    override def compare(that: PieceType): Int = pieceName.compare(that.pieceName)
  }

  object KingType extends PieceType("king")

  object QueenType extends PieceType("queen")

  object BishopType extends PieceType("bishop")

  object KnightType extends PieceType("knight")

  object RookType extends PieceType("rook")

}

object PiecesTypeList {
  private val cache = mutable.Map[String, List[PieceType]]()

  def pieceTypeList(pieceTypes: List[PieceType]): List[PieceType] = {
    val key = new StringBuilder
    val sortedTypes = pieceTypes.sortBy(_.pieceName)
    sortedTypes.foreach { p =>
      key ++= "[" + p.pieceName + "]"
    }

    cache.getOrElseUpdate(key.toString(), sortedTypes)
  }

  def cacheSize = cache.size
}

sealed trait Piece extends Ordered[Piece] {
  val board: Board
  val piecePosition: Position

  val possiblePositionsToAttack: Set[Position]

  def canAttack(position: Position): Boolean = {
    if (position == piecePosition) {
      sys.error("Wrong position, equals to this piece position: " + position)
    }
    possiblePositionsToAttack.contains(position)
  }

  def canAttack(piece: Piece): Boolean = canAttack(piece.piecePosition)

  lazy val pieceName = s"[${getClass.getSimpleName}:${piecePosition.x}:${piecePosition.y}]"

  override def compare(that: Piece): Int = {
    piecePosition.compare(that.piecePosition)
  }
}

object PieceFactory {
  private val pieceCache = mutable.Map[String, Piece]()

  def get(pieceType: PieceType, piecePosition: Position, board: Board) = {
    val key = pieceType.pieceName + ":" + piecePosition.x + "_" + piecePosition.y
    pieceCache.get(key) match {
      case Some(piece) =>
        piece
      case None =>
        val piece = pieceType match {
          case KingType => new King(piecePosition)(board)
          case QueenType => Queen(piecePosition)(board)
          case BishopType => Bishop(piecePosition)(board)
          case KnightType => Knight(piecePosition)(board)
          case RookType => Rook(piecePosition)(board)
          case _ => sys.error("Unknown piece type")
        }
        pieceCache.put(key, piece)
        piece
    }
  }

  def cacheSize = pieceCache.size
}

case class King(val piecePosition: Position)(val board: Board) extends Piece {
  override val possiblePositionsToAttack: Set[Position] = {
    val positionsToAttack = for {
      x <- piecePosition.x - 1 to piecePosition.x + 1
      y <- piecePosition.y - 1 to piecePosition.y + 1
      currentPosition = PositionFactory.position(x, y)
      if board.isPositionOnBoard(currentPosition)
      if piecePosition != currentPosition
    } yield {
      currentPosition
    }

    positionsToAttack.toSet
  }
}

case class Rook(piecePosition: Position)(val board: Board) extends Piece {
  override val possiblePositionsToAttack: Set[Position] = {
    val positionsToAttackX = for {
      y <- 0 to board.width
      x = piecePosition.x
      currentPosition = PositionFactory.position(x, y)
      if board.isPositionOnBoard(currentPosition)
      if piecePosition != currentPosition
    } yield {
      currentPosition
    }

    val positionsToAttackY = for {
      x <- 0 to board.height
      y = piecePosition.y
      currentPosition = PositionFactory.position(x, y)
      if board.isPositionOnBoard(currentPosition)
      if piecePosition != currentPosition
    } yield {
      currentPosition
    }

    (positionsToAttackX ++ positionsToAttackY).toSet
  }
}

case class Bishop(piecePosition: Position)(val board: Board) extends Piece {
  override val possiblePositionsToAttack: Set[Position] = {
    (first() ++ second() ++ third() ++ fourth()).toSet
  }

  def first(): IndexedSeq[Position] = for {
    x <- piecePosition.x - 1 to 0 by -1
    y = piecePosition.y + (piecePosition.x - x)
    currentPosition = PositionFactory.position(x, y)
    if board.isPositionOnBoard(currentPosition)
    if piecePosition != currentPosition
  } yield {
    currentPosition
  }

  def second(): IndexedSeq[Position] = for {
    y <- piecePosition.y - 1 to 0 by -1
    x = piecePosition.x + (piecePosition.y - y)
    currentPosition = PositionFactory.position(x, y)
    if board.isPositionOnBoard(currentPosition)
    if piecePosition != currentPosition
  } yield {
    currentPosition
  }

  def third(): IndexedSeq[Position] = for {
    x <- piecePosition.x - 1 to 0 by -1
    y = piecePosition.y - (piecePosition.x - x)
    currentPosition = PositionFactory.position(x, y)
    if board.isPositionOnBoard(currentPosition)
    if piecePosition != currentPosition
  } yield {
    currentPosition
  }

  def fourth(): IndexedSeq[Position] = for {
    x <- piecePosition.x + 1 to board.width
    y = piecePosition.y + (x - piecePosition.x)
    currentPosition = PositionFactory.position(x, y)
    if board.isPositionOnBoard(currentPosition)
    if piecePosition != currentPosition
  } yield {
    currentPosition
  }
}

case class Queen(piecePosition: Position)(val board: Board) extends Piece {
  override val possiblePositionsToAttack: Set[Position] = {
    val rook = new Rook(piecePosition)(board)
    val bishop = new Bishop(piecePosition)(board)

    rook.possiblePositionsToAttack ++ bishop.possiblePositionsToAttack
  }
}

case class Knight(piecePosition: Position)(val board: Board) extends Piece {
  override val possiblePositionsToAttack: Set[Position] = {
    val pos1 = PositionFactory.position(piecePosition.x + 2, piecePosition.y - 1)
    val pos2 = PositionFactory.position(piecePosition.x + 2, piecePosition.y + 1)
    val pos3 = PositionFactory.position(piecePosition.x - 2, piecePosition.y - 1)
    val pos4 = PositionFactory.position(piecePosition.x - 2, piecePosition.y + 1)

    val pos5 = PositionFactory.position(piecePosition.y + 2, piecePosition.x - 1)
    val pos6 = PositionFactory.position(piecePosition.y + 2, piecePosition.x + 1)
    val pos7 = PositionFactory.position(piecePosition.y - 2, piecePosition.x - 1)
    val pos8 = PositionFactory.position(piecePosition.y - 2, piecePosition.x + 1)

    Set(pos1, pos2, pos3, pos4, pos5, pos6, pos7, pos8)
  }
}
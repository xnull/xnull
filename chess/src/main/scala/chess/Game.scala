package chess

import chess.PiecesType.PieceType

import scala.collection.mutable.ListBuffer
import scala.collection.{SortedSet, mutable}

object Game {

  def check(board: Board, pieceTypes: List[PieceType]) = {
    val parameters = ArrangementParameters(
      SortedSet[Position]() ++ board.allPositions.toSet,
      pieceTypes,
      SortedSet()
    )
    findLayer(parameters, pieceTypes.size, board)
  }

  def findLayer(params: ArrangementParameters, allPiecesNumber: Int, board: Board): Unit = {
    val positionsQueue = mutable.Queue(params)

    var processedPositions = 0
    var ts = System.currentTimeMillis()
    val totalTs = ts

    while (positionsQueue.nonEmpty) {
      processedPositions += 1
      if (processedPositions % 500 == 0) {
        println("Processed positions: " + processedPositions + ", time: " + (System.currentTimeMillis() - ts) + ", stat: " + Statistics.toString)
        ts = System.currentTimeMillis()
      }

      val currLayerParams = positionsQueue.dequeue()
      for (currentPosition <- currLayerParams.restOfAllowedPositions) {
        for (currentPieceType <- currLayerParams.restOfPieceTypes) {
          val piece = PieceFactory.get(currentPieceType, currentPosition, board)

          if (isPieceAllowed(piece, currLayerParams.piecesPositionsOnBoard)) {
            positionsQueue += ArrangementParameters(
              restOfAllowedPositionsOnTheBoard(currLayerParams.restOfAllowedPositions, piece),
              restOfPiecesToArrange(currLayerParams.restOfPieceTypes, currentPieceType),
              currLayerParams.piecesOnBoard + piece
            )
          }
        }
      }

      if (currLayerParams.restOfPieceTypes.isEmpty) {
        if (currLayerParams.piecesOnBoard.size == allPiecesNumber) {
          PiecesOnBoard.add(currLayerParams.piecesOnBoard)
        }
      }
    }

    val time = System.currentTimeMillis() - totalTs
    println("Total positions: " + processedPositions + ", time: " + time + ", performance: " + (processedPositions / time)  + ", stat: " + Statistics.toString)
  }

  def isPieceAllowed(piece: Piece, piecesOnBoard: SortedSet[Position]) = {
    var isAllowed = true
    val iterator = piece.possiblePositionsToAttack.iterator
    var end = iterator.hasNext
    while (!end) {
      if (!iterator.hasNext) {
        end = true
      } else {
        val position = iterator.next
        if (piecesOnBoard.contains(position)) {
          isAllowed = false
          end = true
        }
      }
    }
    isAllowed
  }

  def restOfPiecesToArrange(restOfPieces: List[PieceType], piece: PieceType): List[PieceType] = {
    val types = ListBuffer(restOfPieces: _*) - piece
    PiecesTypeList.pieceTypeList(types.toList)
  }

  def restOfAllowedPositionsOnTheBoard(restOfAllowedPositions: SortedSet[Position], piece: Piece): SortedSet[Position] = {
    val positions = restOfAllowedPositions -- piece.possiblePositionsToAttack - piece.piecePosition
    PositionSetsCache.getPositionSets(positions)
  }
}

case class ArrangementParameters
(
  restOfAllowedPositions: SortedSet[Position],
  restOfPieceTypes: List[PieceType],
  piecesOnBoard: SortedSet[Piece]
) {
  def piecesPositionsOnBoard = piecesOnBoard.map(p => p.piecePosition)
}

object PiecesOnBoard {
  val piecesOnBoard = mutable.SortedSet[String]()
  private var alreadyPrinted = 0

  def add(layer: SortedSet[Piece]) = {
    val key = new StringBuilder
    layer.foreach { p =>
      key ++= p.pieceName
    }

    piecesOnBoard += key.toString()

    if (piecesOnBoard.size % 100 == 0) {
      if (alreadyPrinted != piecesOnBoard.size) {
        alreadyPrinted = piecesOnBoard.size
        println("Found layers: " + piecesOnBoard.size)
      }
    }
  }

  def size = piecesOnBoard.size
}

object Statistics {
  def positionsFactoryCacheSize = PositionFactory.cacheSize

  def positionSetsCache = PositionSetsCache.cacheSize

  def pieceFactoryCache = PieceFactory.cacheSize

  def pieceTypeSets = PiecesTypeList.cacheSize

  def foundLayers = PiecesOnBoard.size

  def positionSetMaxKeySize = PositionSetsCache.maxKeySize

  override def toString = s"Statistics(posititons: $positionsFactoryCacheSize, position sets: $positionSetsCache (max key size: $positionSetMaxKeySize elements), pieces: $pieceFactoryCache pieceType lists: $pieceTypeSets, found layers: $foundLayers)"
}
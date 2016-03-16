package chess

import scala.collection.{mutable, SortedSet}

/**
  * Created by null on 10.01.16.
  */
case class Position(x: Int, y: Int) extends Ordered[Position] {
  override def compare(that: Position): Int = {
    if (x == that.x && y == that.y) {
      0
    } else if (x > that.x) {
      1
    } else if (x == that.x) {
      if (y > that.y) {
        1
      } else {
        -1
      }
    } else {
      -1
    }
  }
}

object PositionFactory {
  private val cache = mutable.HashMap[String, Position]()

  def position(x: Int, y: Int): Position = {
    cache.getOrElseUpdate(s"${x}_$y", Position(x, y))
  }

  def cacheSize = cache.size
}

object PositionSetsCache {
  private val cachePositions = mutable.Map[String, SortedSet[Position]]()
  var maxKeySize = 0

  def getPositionSets(positions: SortedSet[Position]): SortedSet[Position] = {
    val keyString = optimizedStringRepresentation(positions)
    maxKeySize = Math.max(maxKeySize, positions.size)

    cachePositions.getOrElseUpdate(keyString, positions)
  }

  def toPositionsSet(positions: String): SortedSet[Position] = {
    val result = mutable.SortedSet[Position]()
    Range(0, positions.length, 2) foreach { index =>
      result += PositionFactory.position(positions.charAt(index).toString.toInt, positions.charAt(index +1).toString.toInt)
    }
    result
  }

  def optimizedStringRepresentation(positions: SortedSet[Position]): String = {
    val key = new StringBuilder
    positions.foreach { p =>
      key ++= p.x + "" + p.y
    }
    key.toString()
  }

  def cacheSize = cachePositions.size
}

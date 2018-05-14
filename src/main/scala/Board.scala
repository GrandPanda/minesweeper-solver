import scala.collection._
import com.darichey.minesweeper.Matrix

object Board {
  enum State {
    case Revealed(x: Int)
    case Unknown
    case Flag
  }

  def stateToString(state: State): String = state match {
      case State.Revealed(x) => x.toString
      case State.Unknown => "U"
      case State.Flag => "F"
    }
}

class Board(val values: Array[Array[Board.State]]) {

  def apply(y: Int, x: Int) = values(y)(x)

  def around(y: Int, x: Int): Array[(Int, Int)] = {
    Array((y - 1, x - 1), (y - 1, x), (y - 1, x + 1), (y, x - 1), (y, x + 1), (y + 1, x - 1), (y + 1, x), (y + 1, x + 1))
      .filter((a, b) => a >= 0 && b >= 0 && a < values.length && b < values(a).length)
  }

  def borderUnknowns(): Array[(Int, Int)] = {
    val result = mutable.ArrayBuffer.empty[(Int, Int)]
    for (y <- 0 until values.length) {
      for (x <- 0 until values(y).length) {
        if (apply(y, x) == Board.State.Unknown && around(y, x).map(apply).exists(_.isInstanceOf[Board.State.Revealed])) {
          result += ((y, x))
        }
      }
    }
    result.toArray
  }

  def borderRevealed(): Array[(Int, Int, Int)] = {
    val result = mutable.ArrayBuffer.empty[(Int, Int, Int)]
    for (y <- 0 until values.length) {
      for (x <- 0 until values(y).length) {
        if (apply(y, x).isInstanceOf[Board.State.Revealed] && around(y, x).map(apply).exists(_ == Board.State.Unknown)) {
          val num = apply(y, x).asInstanceOf[Board.State.Revealed].x
          result += ((num, y, x))
        }
      }
    }
    return result.toArray
  }

  def asMatrix(unknowns: Array[(Int, Int)] = borderUnknowns(), revealed: Array[(Int, Int, Int)] = borderRevealed()): Matrix = {
    // println(revealed.mkString(" "))
    // println(unknowns.mkString(" "))

    val rows: mutable.ArrayBuffer[Array[Double]] = mutable.ArrayBuffer.empty
    for (t <- revealed) {
      val y = t._2
      val x = t._3
      val around_ = around(y, x)
      val xs = around_.intersect(unknowns)
      val flags = around_.map(apply).count(_ == Board.State.Flag)
      val temp = unknowns.map(t => if (xs.contains(t)) 1.0 else 0.0) :+ (t._1.toDouble - flags)

      // println(temp.mkString(" "))

      rows += temp
    }

    new Matrix(rows.toArray)
  }

  def definiteTiles(): (Array[(Int, Int)], Array[(Int, Int)]) = {
    val unknowns = borderUnknowns()
    val revealed = borderRevealed()
    val eliminated = Matrix.eliminate(asMatrix(unknowns, revealed))

    // println(eliminated)

    val safe = mutable.ArrayBuffer.empty[(Int, Int)]
    val bombs = mutable.ArrayBuffer.empty[(Int, Int)]

    for (row <- eliminated.getValues()) {
      var max = 0.0
      var min = 0.0
      for (col <- row.dropRight(1)) {
        if (col > 0) max = max + col
        if (col < 0) min = min + col
      }

      val constant = row(row.length - 1)
      if (constant == min) {
        safe ++= row.dropRight(1).zipWithIndex.filter(t => t._1 > 0).map(t => unknowns(t._2))
        bombs ++= row.dropRight(1).zipWithIndex.filter(t => t._1 < 0).map(t => unknowns(t._2))
      } else if (constant == max) {
        safe ++= row.dropRight(1).zipWithIndex.filter(t => t._1 < 0).map(t => unknowns(t._2))
        bombs ++= row.dropRight(1).zipWithIndex.filter(t => t._1 > 0).map(t => unknowns(t._2))
      }
    }

    return (safe.distinct.toArray, bombs.distinct.toArray)
  }

  override def toString(): String =
    values.map(_.map(Board.stateToString(_)).mkString).mkString("\n")
}
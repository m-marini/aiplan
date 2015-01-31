package org.mmarini.aiplan

/**
 *
 */
trait EightMove extends Operator {
  /**
   *
   */
  val cost = 1.0

  /**
   *
   */
  def apply(s: EightGame) = {
    val p = s.pos
    val hole = s.hole;
    val tile = hole + offset
    EightGame(p.updated(tile, None).updated(hole, p(tile)))
  }

  def offset: Int
}

object Up extends EightMove {
  val offset = -3
}

object Down extends EightMove {
  val offset = 3
}

object Left extends EightMove {
  val offset = -1
}

object Right extends EightMove {
  val offset = 1
}

object Moves {
  private val map = Map(
    0 -> Set(Right, Down),
    1 -> Set(Right, Down, Left),
    2 -> Set(Left, Down),
    3 -> Set(Right, Down, Up),
    4 -> Set(Right, Down, Left, Up),
    5 -> Set(Left, Down, Up),
    6 -> Set(Right, Up),
    7 -> Set(Right, Left, Up),
    8 -> Set(Left, Up))

  def moves(i: Int): Set[EightMove] = map(i)
}
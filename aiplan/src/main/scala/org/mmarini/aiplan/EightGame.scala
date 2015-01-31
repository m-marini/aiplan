/**
 *
 */
package org.mmarini.aiplan

/**
 * @author us00852
 *
 */
case class EightGame(pos: IndexedSeq[Option[Int]]) extends State[EightMove] {

  val hole = pos.indexOf(None)

  /**
   *
   */
  def apply(m: EightMove) = m(this)

  /**
   *
   */
  def operators: Set[EightMove] = Moves.moves(hole)
}
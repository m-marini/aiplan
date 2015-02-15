/**
 *
 */
package org.mmarini.aiplan.graphplan

/**
 * @author us00852
 *
 */
case class State(propositions: Set[Proposition]) {
  /**
   *
   */
  def apply(op: Operator): State = op(this)

  /**
   *
   */
  def applicable(ops: Set[Operator]): Set[Operator] =
    ops.filter(_.isApplicable(this))

  /**
   *
   */
  def contains(other: State): Boolean = contains(other.propositions)

  /**
   *
   */
  def contains(conditions: Set[Proposition]): Boolean = conditions.subsetOf(propositions)

  /**
   *
   */
  override def toString = s"(${propositions.mkString(" ")})"
}
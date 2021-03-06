package org.mmarini.aiplan.graphplan

import com.sun.tracing.Probe

/**
 *
 */
case class Operator(preconditions: State, addEffects: State, delEffects: State, cost: Double = 1.0) {
  /**
   *
   */
  def apply(state: State): State = {
    require(isApplicable(state))
    state -- delEffects ++ addEffects
  }

  /**
   *
   */
  def isApplicable(state: State): Boolean =
    preconditions.subsetOf(state)

  /**
   * Two operators are indipendent iff:
   * - any negative effects of op1 are neither in preconditions nor positive effects of op2 and
   * - any negative effects of op2 are neither in preconditions and positive effects of op1
   * or in other word
   * - not exists any negative effects of op1 that is a precondition or a positive effect of op2 and
   * - not exists any negative effects of op2 that is a precondition or a positive effect of op1 and
   */
  def isIndipendent(op: Operator): Boolean = {
    val p1 = addEffects.union(preconditions)
    if (op.delEffects.exists(p1.contains)) false
    else {
      val p2 = op.addEffects.union(op.preconditions)
      !delEffects.exists(p2.contains)
    }
  }

  /**
   *
   */
  //  override def toString = s"(+{${addEffects.mkString(" ")}}, -{${delEffects.mkString(" ")}}, ?{${preconditions.mkString(" ")}})"
  override def toString = s"{${addEffects.mkString(" ")}}"
}

/**
 *
 */
object Operator {
  /**
   *
   */
  def apply(p: Proposition): Operator = Operator(Set(p), Set(p), Set(), 0.0)
}
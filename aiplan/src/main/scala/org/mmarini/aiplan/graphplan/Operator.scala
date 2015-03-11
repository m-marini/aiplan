package org.mmarini.aiplan.graphplan

import com.sun.tracing.Probe

/**
 *
 */
case class Operator(preconditions: State, addEffects: State, delEffects: State, descr: String, cost: Double = 1.0) {
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
  override def toString = descr
}

/**
 *
 */
object Operator {

  def apply(preconditions: State, addEffects: State, id: String) =
    new Operator(preconditions, addEffects, Set(), id)

  /**
   *
   */
  def apply(preconditions: State, addEffects: State, delEffects: State, cost: Double) =
    new Operator(
      preconditions, addEffects, Set(),
      s"(+{${addEffects.mkString(", ")}}, -{${delEffects.mkString(", ")}}, ?{${preconditions.mkString(", ")}})",
      cost)

  /**
   *
   */
  def apply(preconditions: State, addEffects: State, delEffects: State) =
    new Operator(
      preconditions, addEffects, Set(),
      s"(+{${addEffects.mkString(", ")}}, -{${delEffects.mkString(", ")}}, ?{${preconditions.mkString(", ")}})")

  /**
   *
   */
  def apply(preconditions: State, addEffects: State): Operator = apply(preconditions, addEffects, Set[String]())

  /**
   *
   */
  def apply(p: String): Operator = new Operator(Set(p), Set(p), Set(), s"nop($p)", 0.0)
}
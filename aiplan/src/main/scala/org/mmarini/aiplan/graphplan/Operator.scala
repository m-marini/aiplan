package org.mmarini.aiplan.graphplan

import com.sun.tracing.Probe

/**
 *
 */
case class Operator(requirements: State, assertions: State, denials: State, descr: String, cost: Double = 1.0) {
  /**
   *
   */
  def apply(state: State): State = {
    require(isApplicable(state))
    state -- denials ++ assertions
  }

  /**
   *
   */
  def isApplicable(state: State): Boolean =
    requirements.subsetOf(state)

  /**
   * Two operators are indipendent iff:
   * - any negative effects of op1 are neither in requirements nor positive effects of op2 and
   * - any negative effects of op2 are neither in requirements and positive effects of op1
   * or in other word
   * - not exists any negative effects of op1 that is a precondition or a positive effect of op2 and
   * - not exists any negative effects of op2 that is a precondition or a positive effect of op1 and
   */
  def isIndipendent(op: Operator): Boolean = {
    val p1 = assertions.union(requirements)
    if (op.denials.exists(p1.contains)) false
    else {
      val p2 = op.assertions.union(op.requirements)
      !denials.exists(p2.contains)
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

  def apply(requirements: State, assertions: State, id: String) =
    new Operator(requirements, assertions, Set(), id)

  /**
   *
   */
  def apply(requirements: State, assertions: State, denials: State, cost: Double) =
    new Operator(
      requirements, assertions, Set(),
      s"(+{${assertions.mkString(", ")}}, -{${denials.mkString(", ")}}, ?{${requirements.mkString(", ")}})",
      cost)

  /**
   *
   */
  def apply(requirements: State, assertions: State, denials: State) =
    new Operator(
      requirements, assertions, Set(),
      s"(+{${assertions.mkString(", ")}}, -{${denials.mkString(", ")}}, ?{${requirements.mkString(", ")}})")

  /**
   *
   */
  def apply(requirements: State, assertions: State): Operator = apply(requirements, assertions, Set[String]())

  /**
   *
   */
  def apply(p: String): Operator = new Operator(Set(p), Set(p), Set(), s"nop($p)", 0.0)
}
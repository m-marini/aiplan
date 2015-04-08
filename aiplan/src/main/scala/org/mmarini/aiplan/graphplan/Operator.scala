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
   * Two operators are independent iff;
   * - any denials of op1 are neither a requirements nor assertions of op2 and
   * - any denials of op2 are neither a requirements nor assertions of op1
   * 
   * or in other word
   * - not exists any denials of op1 that is a requirements or an assertion of op2 and
   * - not exists any denials of op2 that is a requirements or an assertion of op1
   * 
   * Two operators are dependent iff;
   * - exists A denial of op1 that is a requirements or an assertion of op2 or
   * - exists a denial of op2 that is a requirements or an assertion of op1
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
      requirements, assertions, denials,
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
package org.mmarini.aiplan.graphplan

class PlanProblemDSL {
  var _init = Set[String]()

  var _goal = Set[String]()

  class OperatorBuilder(descr: Option[String] = None,
    preconditions: Set[String] = Set(),
    addEffects: Set[String] = Set(),
    delEffects: Set[String] = Set(),
    cost: Double = 1.0) {

    def +?(props: String) = new OperatorBuilder(descr, preconditions + props, addEffects, delEffects, cost)

    def +(props: String) = new OperatorBuilder(descr, preconditions, addEffects + props, delEffects, cost)

    def -(props: String) = new OperatorBuilder(descr, preconditions, addEffects, delEffects + props, cost)

    def cost(c: Double) = new OperatorBuilder(descr, preconditions, addEffects, delEffects, c)

    def apply = descr match {
      case Some(d) => Operator(
        preconditions.map(StringProposition),
        addEffects.map(StringProposition),
        delEffects.map(StringProposition),
        d,
        cost)
      case None => Operator(
        preconditions.map(StringProposition),
        addEffects.map(StringProposition),
        delEffects.map(StringProposition),
        cost = cost)
    }
  }

  var _ops = Seq[OperatorBuilder]()

  /**
   *
   */
  def define(builder: OperatorBuilder) = { _ops = _ops :+ builder }

  /**
   *
   */
  def init(props: String*) { _init ++= props }

  /**
   *
   */
  def goal(props: String*) { _goal ++= props }

  /**
   *
   */
  def op(id: String) = new OperatorBuilder(Some(id))

  /**
   *
   */
  def op = new OperatorBuilder

  /**
   *
   */
  def problem = PlanProblem.createFromBaseOps(_init.map(StringProposition), _goal.map(StringProposition), _ops.map(_.apply).toSet)
}
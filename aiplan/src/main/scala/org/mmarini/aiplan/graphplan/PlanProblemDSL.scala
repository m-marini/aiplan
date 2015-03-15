package org.mmarini.aiplan.graphplan

class PlanProblemDSL {
  var _init = Set[String]()

  var _goal = Set[String]()

  class OperatorBuilder(descr: Option[String] = None,
    requirements: Set[String] = Set(),
    assertions: Set[String] = Set(),
    denials: Set[String] = Set(),
    cost: Double = 1.0) {

    def require(props: String) = new OperatorBuilder(descr, requirements + props, assertions, denials, cost)

    def require(props: Set[String]) = new OperatorBuilder(descr, requirements ++ props, assertions, denials, cost)

    def assert(props: String) = new OperatorBuilder(descr, requirements, assertions + props, denials, cost)

    def assert(props: Set[String]) = new OperatorBuilder(descr, requirements, assertions ++ props, denials, cost)

    def deny(props: String) = new OperatorBuilder(descr, requirements, assertions, denials + props, cost)

    def deny(props: Set[String]) = new OperatorBuilder(descr, requirements, assertions, denials ++ props, cost)

    def cost(c: Double) = new OperatorBuilder(descr, requirements, assertions, denials, c)

    def apply = descr match {
      case Some(d) => Operator(
        requirements,
        assertions,
        denials,
        d,
        cost)
      case None => Operator(
        requirements,
        assertions,
        denials,
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
  def init(props: Set[String]) { _init ++= props }

  /**
   *
   */
  def goal(props: String*) { _goal ++= props }

  /**
   *
   */
  def goal(props: Set[String]) { _goal ++= props }

  /**
   *
   */
  def operator(id: String) = new OperatorBuilder(Some(id))

  /**
   *
   */
  def operator = new OperatorBuilder

  /**
   *
   */
  def apply = PlanProblem.createFromBaseOps(_init, _goal, _ops.map(_.apply).toSet)
}
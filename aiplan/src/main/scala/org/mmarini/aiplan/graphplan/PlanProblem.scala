package org.mmarini.aiplan.graphplan

/**
 *
 */
case class PlanProblem(init: State, goal: State, ops: Set[Operator]) {
  
  lazy val opsMap = ops.map(a => (a.descr, a)).toMap
  
  lazy val propsMap = init ++ goal ++ ops.map(o => o.requirements ++ o.assertions ++ o.denials).flatten
}

/**
 *
 */
object PlanProblem {

  /**
   *
   */
  def createFromBaseOps(init: State, goal: State, baseOps: Set[Operator]): PlanProblem = {
    val baseProps = init ++ goal ++ baseOps.map(o => o.requirements ++ o.assertions ++ o.denials).flatten
    val ops = baseOps ++ baseProps.map(Operator.apply)
    new PlanProblem(init, goal, ops)
  }
}
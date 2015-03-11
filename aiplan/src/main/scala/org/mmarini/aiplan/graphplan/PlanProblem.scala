package org.mmarini.aiplan.graphplan

/**
 *
 */
case class PlanProblem(init: State, goal: State, ops: Set[Operator])

/**
 *
 */
object PlanProblem {

  /**
   *
   */
  def createFromBaseOps(init: State, goal: State, baseOps: Set[Operator]): PlanProblem = {
    val baseProps = init ++ goal ++ baseOps.map(_.requirements).flatten ++ baseOps.map(_.assertions).flatten ++ baseOps.map(_.denials).flatten
    val ops = baseOps ++ baseProps.map(Operator.apply)
    new PlanProblem(init, goal, ops)
  }
}
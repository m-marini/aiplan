package org.mmarini.aiplan.graphplan

/**
 *
 */
case class PlanProblem(init: State, goal: State, ops: Set[Operator])

trait Entity {
  /**
   *
   */
  def init: State = Set()

  /**
   *
   */
  def goal: State = Set()

  /**
   *
   */
  def ops: Set[Operator] = Set()
}

/**
 *
 */
object PlanProblem {

  /**
   *
   */
  def createFromBaseOps(init: State, goal: State, baseOps: Set[Operator]): PlanProblem = {
    val baseProps = init ++ goal ++ baseOps.map(_.preconditions).flatten ++ baseOps.map(_.addEffects).flatten ++ baseOps.map(_.delEffects).flatten
    val ops = baseOps ++ baseProps.map(Operator.apply)
    new PlanProblem(init, goal, ops)
  }

  /**
   *
   */
  def apply(entities: Set[Entity]): PlanProblem = {
    val init = entities.map(_.init).flatten
    val goal = entities.map(_.goal).flatten
    val baseOps = entities.map(_.ops).flatten
    createFromBaseOps(init, goal, baseOps)
  }

}
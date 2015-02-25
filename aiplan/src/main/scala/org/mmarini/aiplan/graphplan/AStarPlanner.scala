/**
 *
 */
package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging

/**
 * @author us00852
 *
 */
class AStarPlanner(problem: PlanProblem, heuristic: (PlanProblem) => Double) extends LazyLogging {

  case class Node(op: Option[Operator], tail: Option[Node]) {
    require(op.isEmpty && tail.isEmpty || !op.isEmpty && !tail.isEmpty)
  }

  /**
   *
   */
  def plan: Plan = ???

  /**
   * 
   */
  def best(n: Int, fringe: Map[State, Node], visited: Set[State]): (Int, Option[Node]) = {
    ???
  }
}
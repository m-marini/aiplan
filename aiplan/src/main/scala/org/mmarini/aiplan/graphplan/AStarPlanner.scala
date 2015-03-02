/**
 *
 */
package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging
import scala.annotation.tailrec

/**
 * @author us00852
 *
 */
class AStarPlanner(problem: PlanProblem, heuristic: (PlanProblem) => Double) extends LazyLogging {

  /**
   * A node in the search graph has the state, path, cost (estimation from initial state to goal, h value (estimation to the goal)
   * and g function (cost from initial state)
   */
  trait Node {
    def g: Double

    def h: Double = heuristic(PlanProblem(state, problem.goal, problem.ops))

    def cost = g + h

    def state: State

    def path: OrderedPlan
  }

  object EmptyNode extends Node {
    val g = 0.0

    def state = problem.init

    val path = Nil
  }

  case class ConcreteNode(op: Operator, prev: Node) extends Node {

    lazy val g = prev.g + op.cost

    lazy val state = op.apply(prev.state)

    lazy val path = op :: prev.path
  }

  /**
   *
   */
  def plan: Option[OrderedPlan] = best(0, Map(problem.init -> EmptyNode), Set()) match {
    case (_, Some(node)) => Some(node.path.reverse)
    case _ => None
  }

  /**
   * The fringe contains the map between the state and the graph node that have to be explored
   * The visited contains the already explored state
   */
  @tailrec
  final def best(n: Int, fringe: Map[State, Node], visited: Set[State]): (Int, Option[Node]) =
    if (fringe.isEmpty)
      (n, None)
    else {
      // Select the node from fringe with lower cost
      val node = fringe.values.reduce((a, b) => if (a.cost <= b.cost) a else b)

      logger.debug(f"exploring iteration   = $n%,d");
      logger.debug(f"          fringe size = ${fringe.size}%,d")
      logger.debug(f"          g           = ${node.g}%,G")
      logger.debug(f"          cost        = ${node.cost}%,G")

      // Check for goal reached
      if (problem.goal.subsetOf(node.state))
        (n, Some(node))
      else {
        // Compute new visited
        val newVisited = visited + node.state
        // for each operator applicable to the selected search node state
        // it creates a new search node and filter out the ones at an already explored state
        val ss = problem.ops
          .filter(_.preconditions.subsetOf(node.state))
          .map(ConcreteNode(_, node).asInstanceOf[Node])
          .filterNot(n => newVisited.contains(n.state))

        /**
         *  Merge a map state, search node with a list of node.
         *  In case of duplicate node it keeps the shorter from initial state
         */
        @tailrec
        def merge(map: Map[State, Node], nodes: Set[Node]): Map[State, Node] =
          if (nodes.isEmpty)
            map
          else {
            // Search for duplicate node
            val duplicateNode = map.get(nodes.head.state)
            // Get the first node from the list
            val node = nodes.head
            // create the new map
            val newMap =
              if (duplicateNode.isEmpty)
                map + (node.state -> node)
              else if (node.g < duplicateNode.get.g)
                (map - (duplicateNode.get.state)) + (node.state -> node)
              else
                map
            // Recursive call 
            merge(newMap, nodes.tail)
          }

        /*
         * Compute the new fringe 
         */
        val newFringe = merge(fringe - node.state, ss)
        best(n + 1, newFringe, newVisited)
      }
    }
}

object Heuristics {

  def hff(problem: PlanProblem): Double = new FFHeuristic(problem).distance

  def h0(problem: PlanProblem): Double = 0.0
}
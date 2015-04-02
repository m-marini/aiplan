package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging
import scala.annotation.tailrec
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Op

/**
 *
 */
class GraphPlanner(problem: PlanProblem) extends LazyLogging {

  lazy val initialGraphPlan = new StateLayer(problem)

  /**
   * Crate a plan for the given problem
   */
  def plan: Option[Plan] = initialGraphPlan.expandUntilPattern(problem.goal) match {
    case None => None
    case Some(initGraph) => {
      val blackList = (1 to initGraph.depth).map(_ => Set[State]()).toList
      search(initGraph, blackList) match {
        case (None, _, _) => None
        case (Some(plan), _, _) => {
          Some(plan.map(_.filterNot(a => a.requirements == a.assertions && a.denials.isEmpty)).reverse)
        }
      }
    }
  }

  /**
   * search and expand the graph until a plan is found or no plan are available
   */
  @tailrec
  final def search(graph: StateLayer, blackList: StateSetList): (Option[Plan], StateLayer, StateSetList) =
    graph.backwardSearch(problem.goal, blackList) match {
      // check for found plan
      case (Some(plan), newBlackList) => (Some(plan), graph, newBlackList)
      case (None, newBlackList) => {
        // expand next layer
        logger.debug(s"No plan found at depth ${graph.depth}, try expanding graph ...")
        val expGraph = graph.next.next
        if (expGraph.isLastLayer) {
          // check for end of graph
          logger.debug("No plan found, no more layer in plan graph.")
          (None, graph, newBlackList)
        } else
          // search next layer 
          search(expGraph, Set[State]() :: newBlackList)
      }
    }
}
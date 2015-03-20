package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging
import scala.annotation.tailrec
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Op

/**
 *
 */
class GraphPlanner(problem: PlanProblem) extends LazyLogging {

  type StateSet = Set[State]
  type StateSetList = List[StateSet]

  lazy val initialGraphPlan = new StateLayer(problem)

  /**
   * Crate a plan for the given problem
   */
  def plan: Option[Plan] = expandToGoal match {
    case None => None
    case Some(initGraph) => {
      search(initGraph, (1 to initGraph.depth).map(_ => Set[State]()).toList) match {
        case (None, _, _) => None
        case (Some(plan), _, _) => {
          Some(plan.map(_.filterNot(a => a.requirements == a.assertions && a.denials.isEmpty)).reverse)
        }
      }

    }
  }

  /**
   * Create a graph plan to reach a layer that satisfy the goal
   */
  def expandToGoal: Option[StateLayer] = {
    def expandTo(graph: StateLayer): Option[StateLayer] = {
      // Expand to next layer
      val expGraph = graph.next.next
      // check for end of graph
      if (expGraph.isLastLayer) {
        val missingProps = problem.goal -- graph.state
        None
      } else if (expGraph.contains(problem.goal)) {
        // check for goal match
        Some(expGraph)
      } else
        // repeat expansion
        expandTo(expGraph)
    }

    expandTo(initialGraphPlan)
  }

  /**
   * search and expand the graph until a plan is found or no plan are available
   */
  @tailrec
  final def search(graph: StateLayer, noGoodTable: StateSetList): (Option[Plan], StateLayer, StateSetList) =
    extractPlan(graph, noGoodTable) match {
      // check for found plan
      case (Some(plan), newNoGoodTable) => (Some(plan), graph, newNoGoodTable)
      case (None, newNoGoodTable) => {
        // expand next layer
        val expGrap = graph.next.next
        if (expGrap.isLastLayer) {
          // check for end of graph
          (None, graph, newNoGoodTable)
        } else
          // search next layer 
          search(expGrap, Set[State]() :: newNoGoodTable)
      }
    }

  /**
   * Extract a plan from a graph
   * It returns the plan if found or the new no good table
   */
  def extractPlan(graph: StateLayer, noGoodTable: StateSetList): (Option[Plan], StateSetList) =
    backwardSearch(graph, problem.goal, noGoodTable)

  /**
   * Given a graph plan and a goal, it searches backward the graph for a plan.
   * It returns the plan or if not found the noGoodTables created while traversing the graph
   */
  def backwardSearch(
    graph: StateLayer,
    goal: State,
    noGoodTable: StateSetList): (Option[Plan], StateSetList) = {

    // Check if initial state layer has reached (no action layer)
    if (graph.isRoot) {
      (Some(Nil), noGoodTable)
    } else if (noGoodTable.head.contains(goal))
      // check if the no goodTable contains the goal
      (None, noGoodTable)
    else {
      gpSearch(graph, goal, Set(), noGoodTable) match {
        // check if no plan has found and add the goal to noGoodTable
        case (None, newNoGoodTable) => (None, (newNoGoodTable.head + goal) :: newNoGoodTable.tail)
        // check for found plan
        case ret => ret
      }
    }
  }

  /**
   * Given a graph plan and the current goal, it searches backward the graph for a plan.
   * It returns the plan or if not found the noGoodTables created while traversing the graph.
   * If the has some propositions it creates an operation set to reduce the proposition of goal
   * When the goal is reduced to empty set it searches for the previous layer
   */
  def gpSearch(
    graph: StateLayer,
    goal: State,
    plan: PartialPlan,
    noGoodTables: StateSetList): (Option[Plan], StateSetList) = {

    // Check for empty goal (search backward previuos layer)
    if (goal.isEmpty) {

      // create the goal for previuos layer
      val nextGoal = plan.map(_.requirements).flatten
      // search backward previuos layer
      backwardSearch(graph.parent.parent, nextGoal, noGoodTables.tail) match {
        // check for no plan found
        case (None, nextNoGood) => (None, noGoodTables.head :: nextNoGood)
        // check for plan found
        case (Some(nextPlan), nextNoGood) => (Some(plan :: nextPlan), noGoodTables.head :: nextNoGood)
      }
    } else {
      val opLayer = graph.parent

      // Select an operator set that satisfies the first goal propositions
      val ops1 = opLayer.providers(goal.head)

      // Filter out the operator mutex with any other operators in the plan
      val ops = if (plan.isEmpty) ops1
      else ops1.filterNot(op => {
        val oo = plan.map((op, _))
        oo.exists(opLayer.mutex)
      })
      val sorted = ops.toList.sortWith(_.cost <= _.cost)

      // check if no operators are available
      // Iterate for an action available
      def loopForAction(ops: List[Operator], noGoodTable: StateSetList): (Option[Plan], StateSetList) =
        if (ops.isEmpty) {
          return (None, noGoodTable)
        } else {
          // Select the operator with lower cost
          val op = ops.head
          // create the new goal removing all precondition from the goal
          val ng = goal -- op.assertions
          // create the new  partial plan 
          val np = plan + op
          // repeat search with the new parms
          gpSearch(graph, ng, np, noGoodTables) match {
            case (Some(plan), newNoGoodTable) => {
              (Some(plan), newNoGoodTable)
            }
            case (None, newNoGoodTable) => loopForAction(ops.tail, newNoGoodTable)
          }
        }

      loopForAction(sorted, noGoodTables)
    }
  }
}
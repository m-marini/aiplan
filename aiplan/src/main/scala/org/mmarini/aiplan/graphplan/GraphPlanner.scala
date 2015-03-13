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
  case class Layer(opLayer: OpLayer, stateLayer: StateLayer)
  type PlanGraph = List[Layer]

  lazy val initialGraphPlan = List(
    Layer(
      OpLayer(Set(), Set()),
      new StateLayer(problem.init)))

  /**
   * Crate a plan for the given problem
   */
  def plan: Option[Plan] = expandToGoal match {
    case None => None
    case Some(initGraph) => {
      search(initGraph, initGraph.map(_ => Set[State]())) match {
        case (None, _, _) => None
        case (Some(plan), _, _) => {
          Some(plan.map(_.filterNot(a => a.requirements == a.assertions && a.denials.isEmpty)).reverse)
        }
      }

    }
  }

  /**
   * Check if two graphs have the same head layer (no more expansion available)
   */
  def isEndGraph(graph: PlanGraph, next: PlanGraph) =
    graph.head.stateLayer.isSameLayer(next.head.stateLayer)

  /**
   * Create a graph plan to reach a layer that satisfy the goal
   */
  def expandToGoal: Option[PlanGraph] = {
    def expandTo(graph: PlanGraph): Option[PlanGraph] = {
      // Expand to next layer
      val expGraph = expandNextLayer(graph)
      val lastStateLayer = graph.head.stateLayer
      val nextStateLayer = expGraph.head.stateLayer
      // check for end of graph
      if (isEndGraph(graph, expGraph)) {
        logger.debug(s"${tab(graph)} Goal [${problem.goal.mkString(",")}] not found in graph plan at depth=${graph.size}")
        None
      } else if (nextStateLayer.contains(problem.goal)) {
        // check for goal match
        logger.debug(s"${tab(graph)} Goal [${problem.goal.mkString(",")}] found in graph plan at depth=${graph.size}")
        Some(expGraph)
      } else
        // repeat expansion
        expandTo(expGraph)
    }

    expandTo(initialGraphPlan)
  }

  /**
   * Expand a plan to next single layer
   */
  def expandNextLayer(graph: PlanGraph): PlanGraph = {
    val ol = graph.head.stateLayer.next(problem.ops)
    val sl = ol.next
    val expGraph = Layer(ol, sl) :: graph
    logger.debug(s"${tab(expGraph)} Expanded graph plan to depth=${expGraph.size}, ${sl.state.size} props, ${sl.mutex.size} mutex, ${ol.ops.size} ops ${ol.mutex.size}, mutex")
    expGraph
  }

  /**
   * search and expand the graph until a plan is found or no plan are available
   */
  @tailrec
  final def search(graph: PlanGraph, noGoodTable: StateSetList): (Option[Plan], PlanGraph, StateSetList) =
    extractPlan(graph, noGoodTable) match {
      // check for found plan
      case (Some(plan), newNoGoodTable) => (Some(plan), graph, newNoGoodTable)
      case (None, newNoGoodTable) => {
        // expand next layer
        val expGrap = expandNextLayer(graph)
        logger.debug(s"${tab(graph)} Expanding graph plan to depth=${expGrap.size}")
        if (isEndGraph(expGrap, graph)) {
          // check for end of graph
          logger.debug(s"${tab(graph)} No more layers in graph plan after depth=${graph.size}")
          (None, graph, newNoGoodTable)
        } // search next layer 
        else search(expGrap, Set[State]() :: newNoGoodTable)
      }
    }

  /**
   * Extract a plan from a graph
   * It returns the plan if found or the new no good table
   */
  def extractPlan(graph: PlanGraph, noGoodTable: StateSetList): (Option[Plan], StateSetList) =
    backwardSearch(graph, problem.goal, noGoodTable)

  /**
   * Given a graph plan and a goal, it searches backward the graph for a plan.
   * It returns the plan or if not found the noGoodTables created while traversing the graph
   */
  def backwardSearch(
    graph: PlanGraph,
    goal: State,
    noGoodTable: StateSetList): (Option[Plan], StateSetList) = {

    // Check if initial state layer has reached (no action layer)
    if (graph.size == 1) {
      logger.debug(s"${tab(graph)} Found plan for goal [${goal.mkString(",")}]")
      (Some(Nil), noGoodTable)
    } else if (noGoodTable.head.contains(goal))
      // check if the no goodTable contains the goal
      (None, noGoodTable)
    else {
      logger.debug(s"${tab(graph)} Searching for goal [${goal.mkString(",")}] at depth=${graph.size}")
      gpSearch(graph, goal, Set(), noGoodTable) match {
        // check if no plan has found and add the goal to noGoodTable
        case (None, newNoGoodTable) => (None, (newNoGoodTable.head + goal) :: newNoGoodTable.tail)
        // check for found plan
        case ret => ret
      }
    }
  }

  def tab(graph: PlanGraph) = (1 to graph.size).map(_ => ' ').mkString

  /**
   * Given a graph plan and the current goal, it searches backward the graph for a plan.
   * It returns the plan or if not found the noGoodTables created while traversing the graph.
   * If the has some propositions it creates an operation set to reduce the proposition of goal
   * When the goal is reduced to empty set it searches for the previous layer
   */
  def gpSearch(
    graph: PlanGraph,
    goal: State,
    plan: PartialPlan,
    noGoodTables: StateSetList): (Option[Plan], StateSetList) = {

    // Check for empty goal (search backward previuos layer)
    if (goal.isEmpty) {

      // create the goal for previuos layer
      val nextGoal = plan.map(_.requirements).flatten
      // search backward previuos layer
      backwardSearch(graph.tail, nextGoal, noGoodTables.tail) match {
        // check for no plan found
        case (None, nextNoGood) => (None, noGoodTables.head :: nextNoGood)
        // check for plan found
        case (Some(nextPlan), nextNoGood) => (Some(plan :: nextPlan), noGoodTables.head :: nextNoGood)
      }
    } else {
      val opLayer = graph.head.opLayer
      logger.debug(s"${tab(graph)} Searching for [${goal.head}] at depth=${graph.size} ...")

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
          logger.debug(s"${tab(graph)} No more operators satisfying [${goal.head}] at depth=${graph.size}")
          return (None, noGoodTable)
        } else {
          // Select the operator with lower cost
          val op = ops.head
          logger.debug(s"${tab(graph)} Selected operator [$op] satisfying [${goal.head}] at depth=${graph.size}")
          // create the new goal removing all precondition from the goal
          val ng = goal -- op.assertions
          // create the new  partial plan 
          val np = plan + op
          // repeat search with the new parms
          logger.debug(s"${tab(graph)} Searching for remaining goal [$ng] at depth=${graph.size}")
          gpSearch(graph, ng, np, noGoodTables) match {
            case (Some(plan), newNoGoodTable) => {
              logger.debug(s"${tab(graph)} Selected plan [$plan] for [${goal.mkString(",")}] at depth=${graph.size}")
              (Some(plan), newNoGoodTable)
            }
            case (None, newNoGoodTable) => loopForAction(ops.tail, newNoGoodTable)
          }
        }

      loopForAction(sorted, noGoodTables)
    }
  }
}
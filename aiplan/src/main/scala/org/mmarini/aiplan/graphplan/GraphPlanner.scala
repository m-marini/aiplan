package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging

/**
 *
 */
class GraphPlanner(problem: PlanProblem) extends LazyLogging {

  type StateSet = Set[State]
  type StateSetList = List[StateSet]
  type Layer = (StateLayer, Option[OpLayer])
  type PlanGraph = List[Layer]

  /**
   * Crate a plan for the given problem
   */
  def plan: Option[Plan] = expandToGoal match {
    case None => None
    case Some(initGraph) => {
      search(initGraph, initGraph.map(_ => Set[State]())) match {
        case (None, _, _) => None
        case (Some(plan), _, _) => {
          Some(plan.map(_.filterNot(a => a.preconditions == a.addEffects && a.delEffects.isEmpty)).reverse)
        }
      }

    }
  }

  /**
   * Check if two graphs have the same head layer (no more expansion available)
   */
  def isEndGraph(graph: PlanGraph, next: PlanGraph) = graph.head match {
    case (last, _) => last.isSameLayer(next.head._1)
  }

  /**
   * Create a graph plan to reach a layer that satisfy the goal
   */
  def expandToGoal: Option[PlanGraph] = {
    def expandTo(graph: PlanGraph): Option[PlanGraph] = {
      // Expand to next layer
      val expGraph = expandNextLayer(graph)
      val (lastStateLayer, _) = graph.head
      val (nextStateLayer, _) = expGraph.head
      // check for end of graph
      if (isEndGraph(graph, expGraph)) None
      // check for goal match
      else if (nextStateLayer.contains(problem.goal)) Some(expGraph)
      // repeat expansion
      else expandTo(expGraph)
    }

    expandTo(List((new StateLayer(problem.init), None)))
  }

  /**
   * Expand a plan to next single layer
   */
  def expandNextLayer(graph: PlanGraph): PlanGraph = {
    val ol = graph.head._1.next(problem.ops)
    (ol.next, Some(ol)) :: graph
  }

  /**
   * search and expand the graph until a plan is found or no plan are available
   */
  def search(graph: PlanGraph, noGoodTable: StateSetList): (Option[Plan], PlanGraph, StateSetList) =
    extractPlan(graph, noGoodTable) match {
      // check for found plan
      case (Some(plan), newNoGoodTable) => (Some(plan), graph, newNoGoodTable)
      case (None, newNoGoodTable) => {
        // expand next layer
        val expGrap = expandNextLayer(graph)
        // check for end of graph
        if (isEndGraph(expGrap, graph)) (None, graph, newNoGoodTable)
        // search next layer 
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

    logger.debug(f"backwardSearch for ${goal.mkString(" ")}")
    logger.debug(f"               in ${graph.head._1}")

    // Check if initial state layer has reached (no action layer)
    if (graph.head._2.isEmpty) (Some(Nil), noGoodTable)
    // check if the no goodTable contains the goal
    else if (noGoodTable.head.contains(goal)) (None, noGoodTable)
    else
      gpSearch(graph, goal, Set(), noGoodTable) match {
        // check if no plan has found and add the goal to noGoodTable
        case (None, newNoGoodTable) => (None, (newNoGoodTable.head + goal) :: newNoGoodTable.tail)
        // check for found plan
        case ret => ret
      }
  }

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

    logger.debug(f"gpSearch goal={${goal.mkString(" ")}}")
    logger.debug(f"         current operation set={${plan.mkString(" ")}}")
    // Check for empty goal (search backward previuos layer)
    if (goal.isEmpty) {

      // create the goal for previuos layer
      val nextGoal = plan.map(_.preconditions).flatten
      // search backward previuos layer
      backwardSearch(graph.tail, nextGoal, noGoodTables.tail) match {
        // check for no plan found
        case (None, nextNoGood) => (None, noGoodTables.head :: nextNoGood)
        // check for plan found
        case (Some(nextPlan), nextNoGood) => (Some(plan :: nextPlan), noGoodTables.head :: nextNoGood)
      }
    } else {
      val (_, Some(opLayer)) = graph.head

      // Select an operator set that satisfies the first goal propositions
      logger.debug(s"         Selected prop ${goal.head}")
      val ops1 = opLayer.providers(goal.head)
      logger.debug(s"         prefiltered operation set={${ops1.mkString(" ")}}")

      // Filter out the operator mutex with any other operators in the plan
      val ops = if (plan.isEmpty) ops1
      else ops1.filter(op => {
        val inter = plan.map((op, _)).intersect(opLayer.mutex)
        inter.isEmpty
      })
      logger.debug(s"         filtered operation set={${ops.mkString(" ")}}")

      // check if no operators are available
      if (ops.isEmpty) (None, noGoodTables)
      else {
        // Iterate for an action available
        def loopForAction(ops: Set[Operator], noGoodTable: StateSetList): (Option[Plan], StateSetList) =
          if (ops.isEmpty)
            return (None, noGoodTable)
          else {
            // Select the first operator
            val op = ops.head
            logger.debug(s"         Selected op $op")
            // create the new goal removing all precondition from the goal
            val ng = goal -- op.addEffects
            // create the new  partial plan 
            val np = plan + op
            // repeat search with th new parms
            gpSearch(graph, ng, np, noGoodTables) match {
              case (Some(plan), newNoGoodTable) => (Some(plan), newNoGoodTable)
              case (None, newNoGoodTable) => loopForAction(ops.tail, newNoGoodTable)
            }
          }

        loopForAction(ops, noGoodTables)
      }
    }
  }
}
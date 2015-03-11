package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging
import scala.annotation.tailrec

/**
 *
 */
class FFHeuristic(problem: PlanProblem) extends LazyLogging {

  type Layer = (State, PartialPlan)
  type GraphPlan = List[Layer]

  /**
   * Estimate the distance of problem using FF algorithm
   */
  def distance: Double = {
    val rpg = expandRPG(List((problem.init, Set())))
    val fog = computeFOG(rpg)
    extractRPSize(fog)
  }

  /**
   * Expand the reduced plan graph
   */
  @tailrec
  final def expandRPG(graph: GraphPlan): GraphPlan = {
    val (stateLayer, _) = graph.head
    if (problem.goal.subsetOf(stateLayer)) graph
    else {
      val opLayer = problem.ops.filter(_.requirements.subsetOf(stateLayer))
      val next = opLayer.flatMap(_.assertions)
      if (next.size == stateLayer.size) Nil
      else expandRPG((next, opLayer) :: graph)
    }
  }

  /**
   * Compute the first only plan graph
   */
  def computeFOG(graph: GraphPlan): GraphPlan = {
    val reverse = graph.reverse
    reduceFirstLayer(List(reverse.head), reverse, problem.goal)._1
  }

  /**
   * Reduce the reduced plan graph to first only plan graph
   */
  @tailrec
  final def reduceFirstLayer(flg: GraphPlan, rpg: GraphPlan, goal: State): (GraphPlan, GraphPlan, State) =
    if (goal.isEmpty || rpg.size <= 1)
      (flg, rpg, goal)
    else {
      val (sl0, al0) = rpg.head
      val (sl1, al1) = rpg(1)
      val fl = (sl1.diff(sl0), al1.diff(al0))
      val ng = goal.diff(fl._1)
      reduceFirstLayer(fl :: flg, rpg.tail, ng)
    }

  /**
   * extract the cost of reduced plan by rpg
   */
  def extractRPSize(plan: GraphPlan): Double = {
    val layers = plan.map {
      case (s, o) => (s, o, s.intersect(problem.goal))
    }
    extractPlan(Set(), layers)._1.map(_.cost).sum
  }

  /**
   * Extract the plan by first only plan and goal layers
   */
  @tailrec
  final def extractPlan(plan: PartialPlan, layers: List[(State, PartialPlan, State)]): (PartialPlan, List[(State, PartialPlan, State)]) =
    if (layers.isEmpty)
      (plan, layers)
    else {
      val (states, ops, goals) = layers.head
      val selOps = goals.map(prop => ops.find(_.assertions.contains(prop))).filterNot(_.isEmpty).map(_.get)
      val npl = selOps ++ plan
      val newGoals = selOps.map(_.requirements).flatten
      val newLayers = layers.tail.map {
        case (s, o, g) => {
          (s, o, g ++ (newGoals.intersect(s)))
        }
      }
      extractPlan(npl, newLayers)
    }
}
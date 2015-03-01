package org.mmarini.aiplan.graphplan

/**
 *
 */
object ThreeProblem extends TileGame(2, 2) {

  val goal = state("- 1 2 3")

  val init = state("1 3 - 2")

  val problem = PlanProblem(init, goal, ops)
}
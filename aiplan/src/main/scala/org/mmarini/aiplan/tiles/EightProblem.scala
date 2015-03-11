package org.mmarini.aiplan.tiles

import org.mmarini.aiplan.graphplan.PlanProblem

/**
 *
 */
object EightProblem extends TileGame(3, 3) {
  val goal = state("- 1 2 3 4 5 6 7 8")

  val init = state("7 2 4 5 - 6 8 3 1")

  val problem = PlanProblem(init, goal, ops)
}
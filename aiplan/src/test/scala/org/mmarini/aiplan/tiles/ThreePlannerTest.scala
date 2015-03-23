package org.mmarini.aiplan.tiles

import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.GraphPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class ThreePlannerTest extends FunSpec with Matchers {

  val problem = new TileGame(2, 2) {
    init(state("1 3 - 2"))
  }.apply

  describe("A graph planner") {

    val planner = new GraphPlanner(problem)

    describe("the expand to goal") {
      val etgOpt = planner.initialGraphPlan.expandUntilPattern(problem.goal)
      they("should exists") {
        etgOpt should not be empty
        etgOpt.get.depth should be(4)
      }
    }

    describe("the plan") {
      val planOpt = planner.plan

      it("should exist") {
        planOpt should not be empty
        planOpt.get.size should be(3)
      }
    }
  }

  describe("A AStarPlanner") {
    val planner = new AStarPlanner(problem, Heuristics.hff)

    describe("the planner") {
      val planOpt = planner.plan

      it("should exist and have size 3") {
        planOpt should not be empty
        planOpt.get should have size (3)
        println(planOpt.get)
      }
    }
  }
}
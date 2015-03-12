package org.mmarini.aiplan.tiles

import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.GraphPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.scalatest.FunSpec
import org.scalatest.Matchers

import ThreeProblem.problem

/**
 *
 */
class ThreePlannerTest extends FunSpec with Matchers {
  describe("A graph planner") {
    import ThreeProblem._
    val planner = new GraphPlanner(problem)

    describe("the expand to goal") {
      val etgOpt = planner.expandToGoal
      they("should exists") {
        etgOpt should not be empty
        etgOpt.get.size should be(4)
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

//  describe("A AStarPlanner") {
//    val planner = new AStarPlanner(ThreeProblem.problem, Heuristics.hff)
//
//    describe("the planner") {
//      val planOpt = planner.plan
//
//      it("should exist and have size 3") {
//        planOpt should not be empty
//        planOpt.get should have size (3)
//        println(planOpt.get)
//      }
//    }
//  }
}
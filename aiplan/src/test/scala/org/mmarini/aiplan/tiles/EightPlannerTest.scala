package org.mmarini.aiplan.tiles

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.mmarini.aiplan.graphplan.GraphPlanner

/**
 *
 */
class EightPlannerTest extends FunSpec with Matchers {
  describe("A graph planner") {
    import EightProblem._
    
    val problem = EightProblem.apply
    
    val planner = new GraphPlanner(problem)

    describe("the plan") {
      val planOpt = planner.plan

      it("should exist and have size 6") {
        planOpt should not be empty
        planOpt.get should have size (6)
      }
    }
  }
  
//
//  describe("A AStarPlanner") {
//    val planner = new AStarPlanner(EightProblem.problem, Heuristics.hff)
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
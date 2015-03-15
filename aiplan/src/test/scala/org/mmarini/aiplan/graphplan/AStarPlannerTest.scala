package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.dwr.DWRProblem

/**
 *
 */
class AStarPlannerTest extends FunSpec with Matchers {
  describe("Defined the DWR Problem and a planner") {
    val problem = DWRProblem.apply

    val planner = new AStarPlanner(problem, Heuristics.hff)

    describe("the planner") {
      val planOpt = planner.plan

      it("should have size 6") {
        planOpt should not be empty
        planOpt.get should have size 6
      }
    }
  }
}
package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class EightPlannerTest extends FunSpec with Matchers {
  describe("A graph planner") {
    import EightProblem._
    val planner = new GraphPlanner(problem)

    describe("the plan") {
      val planOpt = planner.plan

      it("should exist and have size 6") {
        planOpt should not be empty
        planOpt.get should have size (6)
      }
    }
  }
}
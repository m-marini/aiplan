package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class PlannerTest extends FunSpec with Matchers {
  describe("Defined the DWR Problem and a planner") {
    import DWRProblem._
    val planner = new Planner(problem)

    describe("the first layer seq") {
      val initGraphOpt = planner.expandToGoal

      it("should not be empty") {
        initGraphOpt should not be empty
      }

      it("should have size 4") {
        initGraphOpt.get should have size 4
      }

      describe("the extract plan from inital graph") {
        val (plan, _) = planner.extractPlan(initGraphOpt.get, initGraphOpt.get.map(_ => Set[State]()));
        println(plan.get.mkString("\n"))

        it("should exist") {
          plan should not be empty
        }
      }
    }
  }
}
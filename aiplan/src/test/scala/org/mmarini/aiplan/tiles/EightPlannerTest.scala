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
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Up$@32c88e0d
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Down$@5488bcae
  //org.mmarini.aiplan.Down$@5488bcae
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Up$@32c88e0d
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Up$@32c88e0d
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Down$@5488bcae
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Down$@5488bcae
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Up$@32c88e0d
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Right$@ce61187
  //org.mmarini.aiplan.Up$@32c88e0d
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Down$@5488bcae
  //org.mmarini.aiplan.Left$@1dae9d0c
  //org.mmarini.aiplan.Up$@32c88e0d

  //    val problem = new TileGame(3, 3) {
  //      init(state("7 2 4 5 - 6 8 3 1"))
  //    }

  describe("A graph planner") {
    val problem = new TileGame(3, 3) {
      // 3 1 2
      // - 4 5
      // 6 7 8
      init(state("3 1 2 - 4 5 6 7 8 9"))
    }.apply

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
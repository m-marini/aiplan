package org.mmarini.aiplan.tiles

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.mmarini.aiplan.graphplan.GraphPlanner

/**
 *
 */
class EightPlannerByStepTest extends FunSpec with Matchers {
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
  // 39 - 13 = 26

  //    val problem = new TileGame(3, 3) {
  //      init(state("7 2 4 5 - 6 8 3 1"))
  //    }

  describe("A graph planner 15") {
    val problem = new TileGame(3, 3) {

      // 2 5 -
      // 3 6 4
      // 7 8 1
      // [5] org.mmarini.aiplan.Left$@1dae9d0c
      // 2 - 5
      // 3 6 4
      // 7 8 1
      // [2] org.mmarini.aiplan.Left$@1dae9d0c
      // - 2 5
      // 3 6 4
      // 7 8 1
      // [3] org.mmarini.aiplan.Down$@5488bcae
      // 3 2 5
      // - 6 4
      // 7 8 1
      // [6] org.mmarini.aiplan.Right$@ce61187
      // 3 2 5
      // 6 - 4
      // 7 8 1
      // [4] org.mmarini.aiplan.Right$@ce61187
      // 3 2 5
      // 6 4 -
      // 7 8 1
      // [1] org.mmarini.aiplan.Down$@5488bcae
      //      init(state("3 2 5 6 4 1 7 8 -"))
      // 3 2 5
      // 6 4 1
      // 7 8 -
      // [8] org.mmarini.aiplan.Left$@1dae9d0c
      init(state("3 2 5 6 4 1 7 - 8"))
      // 3 2 5
      // 6 4 1
      // 7 - 8
      // [7] org.mmarini.aiplan.Left$@1dae9d0c
      //            init(state("3 2 5 6 4 1 - 7 8"))
      // 3 2 5
      // 6 4 1
      // - 7 8
      // [6] org.mmarini.aiplan.Up$@32c88e0d
      // 3 2 5
      // - 4 1
      // 6 7 8
      // [4] org.mmarini.aiplan.Right$@ce61187
      // 3 2 5
      // 4 - 1
      // 6 7 8
      // [1] org.mmarini.aiplan.Right$@ce61187
      // 3 2 5
      // 4 1 -
      // 6 7 8
      // [5] org.mmarini.aiplan.Up$@32c88e0d
      //            init(state("3 2 - 4 1 5 6 7 8"))
      // 3 2 -
      // 4 1 5
      // 6 7 8
      // [2] org.mmarini.aiplan.Left$@1dae9d0c
      // 3 - 2
      // 4 1 5
      // 6 7 8
      // [1] org.mmarini.aiplan.Down$@5488bcae
      // 3 1 2
      // 4 - 5
      // 6 7 8
      // [4] org.mmarini.aiplan.Left$@1dae9d0c
      //      init(state("3 1 2 - 4 5 6 7 8"))
      // 3 1 2
      // - 4 5
      // 6 7 8
      // [3] org.mmarini.aiplan.Up$@32c88e0d
      // - 1 2
      // 3 4 5
      // 6 7 8

    }.apply

    val planner = new GraphPlanner(problem)

    describe("the plan") {
      val planOpt = planner.plan

      it("should exist and have size 1") {
        planOpt should not be empty
        val descr = planOpt.get.flatten.map(_.descr)
        descr should contain theSameElementsInOrderAs (List(
          //          "Move [5]",
          //          "Move [2]",
          //          "Move [3]",
          //          "Move [6]",
          //          "Move [4]",
          //          "Move [1]",
          //          "Move [8]",
          "Move [7]",
          "Move [6]",
          "Move [4]",
          "Move [1]",
          "Move [5]",
          "Move [2]",
          "Move [1]",
          "Move [4]",
          "Move [3]"))
      }
    }
  }
}
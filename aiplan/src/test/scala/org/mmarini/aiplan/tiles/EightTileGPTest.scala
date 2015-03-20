package org.mmarini.aiplan.tiles

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.mmarini.aiplan.graphplan.GraphPlanner
import org.mmarini.aiplan.graphplan.StateLayer
import org.mmarini.aiplan.graphplan.OpLayer

/**
 *
 */
class EightTileGPTest extends FunSpec with Matchers {

  describe("A graph planner 1") {
    val problem = new TileGame(3, 3) {
      // 3 1 2
      // - 4 5
      // 6 7 8
      init(state("3 1 2 - 4 5 6 7 8 9"))
    }.apply

    val planner = new GraphPlanner(problem)

    describe("the first op layer after filtered for no nop") {
      val ops = planner.initialGraphPlan.next.ops.map(_.descr).filterNot(_.startsWith("nop("))
      it("should contian ...") {
        ops should contain("Move [3]")
        ops should contain("Move [4]")
        ops should contain("Move [6]")
      }
      it("should have size 3") {
        ops should have size (3)
      }

    }

    describe("the 2nd state layer") {
      val p0 = planner.initialGraphPlan
      val p1 = p0.next.next

      describe("the props after removing previuos state layer props") {
        val states = p1.state -- p0.state

        it("should contian ...") {
          states should contain("[3] at (1,0)")
          states should contain("[4] at (1,0)")
          states should contain("[6] at (1,0)")
          states should contain("[ ] at (0,0)")
          states should contain("[ ] at (1,1)")
          states should contain("[ ] at (2,0)")
        }
        it("should have size 3") {
          states should have size (6)
        }
      }
      describe("the states") {
        val states = p1.state
        it("should match the goal") {
          for (p <- problem.goal)
            states should contain(p)
        }
      }
    }
  }
}
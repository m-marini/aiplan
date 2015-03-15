package org.mmarini.aiplan.tiles

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.mmarini.aiplan.graphplan.GraphPlanner
import org.mmarini.aiplan.graphplan.StateLayer
import org.mmarini.aiplan.graphplan.OpLayer
import EightProblem._

/**
 *
 */
class EightTileGPTest extends FunSpec with Matchers {

  val problem = EightProblem.apply
  
  describe("A graph planner") {
    val planner = new GraphPlanner(problem)
    // 7 2 4
    // 5 - 6
    // 8 3 1
    describe("the depth 1 graph plan") {
      val p1 = planner.initialGraphPlan

      describe("the layer") {

        val l = p1.head
        it("should have no ops") {
          l.opLayer.ops shouldBe empty
        }

        val sl = l.stateLayer
        it("should contain ...") {
          sl.state should have size 9
          sl.state should contain("[7] at (0,0)")
          sl.state should contain("[2] at (0,1)")
          sl.state should contain("[4] at (0,2)")
          sl.state should contain("[5] at (1,0)")
          sl.state should contain("[ ] at (1,1)")
          sl.state should contain("[6] at (1,2)")
          sl.state should contain("[8] at (2,0)")
          sl.state should contain("[3] at (2,1)")
        }
        it("should contain 9 props") {
          sl.state should contain("[1] at (2,2)")
        }
      }

      describe("the depth 2 graph plan") {
        val p2 = planner.expandNextLayer(p1)

        describe("the op layer filtered for no nop") {
          val ops = p2.head.opLayer.ops.map(_.descr).filterNot(_.startsWith("nop("))
          // Move 2, 5, 6, 3 to 1,1
          it("should have 4 ops: ... ") {
            ops should contain("Move [2] to (1,1)")
            ops should contain("Move [5] to (1,1)")
            ops should contain("Move [6] to (1,1)")
            ops should contain("Move [3] to (1,1)")
            ops should have size 4
          }
        }
        describe("the state layer after removed previous state layer") {
          val s = p2.head.stateLayer.state -- p2(1).stateLayer.state
          // 7 - 4
          // 5 2 6
          // 8 3 1

          // 7 2 4
          // - 5 6
          // 8 3 1

          // 7 2 4
          // 5 6 -
          // 8 3 1

          // 7 2 4
          // 5 3 6
          // 8 - 1

          it("should contain ...") {
            s should contain("[ ] at (0,1)")
            s should contain("[ ] at (1,0)")
            s should contain("[ ] at (1,2)")
            s should contain("[ ] at (2,1)")
            s should contain("[2] at (1,1)")
            s should contain("[5] at (1,1)")
            s should contain("[3] at (1,1)")
            s should contain("[6] at (1,1)")
          }
          it("should contain 8 props") {
            s should have size 8
          }
        }

        describe("the depth 3 graph plan") {
          val p3 = planner.expandNextLayer(p2)

          describe("the op layer after removing previuos layer op and filtered for no nop") {
            val ops = p3.head.opLayer.ops.--(p3(1).opLayer.ops).
              map(_.descr).filterNot(_.startsWith("nop("))

            // Move 7 7 4 4 8 8 1 1 
            it("should contain ... ") {
              ops should contain("Move [7] to (0,1)")
              ops should contain("Move [7] to (1,0)")
              ops should contain("Move [4] to (0,1)")
              ops should contain("Move [4] to (1,2)")
              ops should contain("Move [8] to (1,0)")
              ops should contain("Move [8] to (2,1)")
              ops should contain("Move [1] to (1,2)")
              ops should contain("Move [1] to (2,1)")
              ops should contain("Move [2] to (0,1)")
              ops should contain("Move [5] to (1,0)")
              ops should contain("Move [6] to (1,2)")
              ops should contain("Move [3] to (2,1)")
            }
            it("should contain 12 ops") {
              ops should have size 12
            }

          }
          describe("the state layer after removed previous state layer") {
            val s = p3.head.stateLayer.state -- p3(1).stateLayer.state
            // - 7 4
            // 5 2 6
            // 8 3 1

            // 7 4 -
            // 5 2 6
            // 8 3 1

            // - 2 4
            // 7 5 6
            // 8 3 1

            // 7 2 4
            // 8 5 6
            // - 3 1

            // 7 2 -
            // 5 6 4
            // 8 3 1

            // 7 2 4
            // 5 6 1
            // 8 3 -

            // 7 2 4
            // 5 3 6
            // - 8 1

            // 7 2 4
            // 5 3 6
            // 8 1 -

            it("should contain ...") {
              s should contain("[ ] at (0,0)")
              s should contain("[ ] at (0,2)")
              s should contain("[ ] at (2,0)")
              s should contain("[ ] at (2,2)")
              s should contain("[7] at (0,1)")
              s should contain("[7] at (1,0)")
              s should contain("[4] at (0,1)")
              s should contain("[4] at (1,2)")
              s should contain("[8] at (1,0)")
              s should contain("[8] at (2,1)")
              s should contain("[1] at (1,2)")
              s should contain("[1] at (2,1)")
            }
            it("should contain 12 props") {
              s should have size 12
            }
          }
        }
      }
    }
  }
}
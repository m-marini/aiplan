package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.dwr.DWRProblem

/**
 *
 */
class FFHeuristicTest extends FunSpec with Matchers {
  describe("Defined the heuristic on the DWR problem") {
    import DWRProblem._

    val problem = DWRProblem.apply

    val heuristic = new FFHeuristic(problem)

    describe("the rpg") {
      val rpg = heuristic.expandRPG(List((problem.init, Set())))

      it("should not be empty and have size 3") {
        rpg should have size 3
      }

      describe("the first only graph") {
        val fog = heuristic.computeFOG(rpg)

        it("should have size 3") {
          fog should have size 3
        }

        describe("the rp size") {
          val rps = heuristic.extractRPSize(fog)

          it("should have size 6") {
            rps should be(6)
          }
        }
      }
    }

    describe("the distance") {
      val distance = heuristic.distance

      it("should be 6") {
        distance should be(6)
      }
    }
  }
}
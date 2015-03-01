package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class ThreePlannerTest extends FunSpec with Matchers {
  describe("A graph planner") {
    import ThreeProblem._
    val planner = new GraphPlanner(problem)

    describe("the second op layer") {
      val opl2 = new StateLayer(problem.init).next(problem.ops)

      it("ops should have size 6") {
        opl2.ops should have size 6
      }

      describe("mutex") {
        val mutex = opl2.mutex

        it("mutex should contain ...") {
          mutex should contain((hole.move((1, 0), (0, 0), 0), Operator(hole.at(1, 0))))
          mutex should contain((hole.move((1, 0), (0, 0), 0), Operator(tiles(0).at(0, 0))))
          mutex should contain((hole.move((1, 0), (1, 1), 1), Operator(hole.at(1, 0))))
          mutex should contain((hole.move((1, 0), (1, 1), 1), Operator(tiles(1).at(1, 1))))
          mutex should contain((hole.move((1, 0), (0, 0), 0), hole.move((1, 0), (1, 1), 1)))
        }

        it("mutex should have size 10") {
          mutex should have size (10)
        }
      }

      describe("the second state layer") {
        val sl2 = opl2.next

        describe("the state") {
          val state = sl2.state

          it("should have size 8") {
            state should have size (8)
          }

          it("should contain ...") {
            state should contain(hole.at(0, 0))
            state should contain(hole.at(1, 1))
            state should contain(tiles(0).at(1, 0))
            state should contain(tiles(1).at(1, 0))
          }
        }

        describe("the mutex") {
          val mutex = sl2.mutex

          it("should contain ...") {
            // Same location
            mutex should contain((hole.at(0, 0), tiles(0).at(0, 0)))
            mutex should contain((hole.at(1, 1), tiles(1).at(1, 1)))
            mutex should contain((hole.at(1, 0), tiles(0).at(1, 0)))
            mutex should contain((hole.at(1, 0), tiles(1).at(1, 0)))
            mutex should contain((tiles(0).at(1, 0), tiles(1).at(1, 0)))

            // Same object
            mutex should contain((tiles(0).at(1, 0), tiles(0).at(0, 0)))
            mutex should contain((tiles(1).at(1, 1), tiles(1).at(1, 0)))
            mutex should contain((hole.at(0, 0), hole.at(1, 0)))
            mutex should contain((hole.at(0, 0), hole.at(1, 1)))
            mutex should contain((hole.at(1, 0), hole.at(1, 1)))

            mutex should contain((hole.at(0, 0), tiles(1).at(1, 0)))
            mutex should contain((hole.at(1, 1), tiles(0).at(1, 0)))

          }

          it("should have size 24") {
            mutex should have size (24)
          }
        }

        describe("the third op layer") {
          val opl3 = sl2.next(problem.ops)

          describe("the ops") {
            val ops = opl3.ops

            it("ops should have size 6+8") {
              ops should have size (6 + 8)
            }

            it("ops should contain ...") {
              ops should contain(Operator(hole.at(0, 0)))
              ops should contain(Operator(hole.at(1, 1)))
              ops should contain(Operator(tiles(0).at(1, 0)))
              ops should contain(Operator(tiles(1).at(1, 0)))
              ops should contain(hole.move((0, 0), (0, 1), 2))
              ops should contain(hole.move((0, 0), (1, 0), 0))
              ops should contain(hole.move((1, 1), (0, 1), 2))
              ops should contain(hole.move((1, 1), (1, 0), 1))
            }
          }

          describe("the mutex") {
            val mutex = opl3.mutex

            it("should contain ...") {
              mutex should contain(hole.move((0, 0), (0, 1), 2), Operator(hole.at(0, 0)))
              mutex should contain(hole.move((0, 0), (0, 1), 2), Operator(tiles(2).at(0, 1)))
              mutex should contain(hole.move((1, 1), (0, 1), 2), Operator(hole.at(1, 1)))
              mutex should contain(hole.move((1, 1), (0, 1), 2), Operator(tiles(2).at(0, 1)))
            }
          }
        }
      }
    }

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
        println(planOpt.get)
      }
    }
  }
}
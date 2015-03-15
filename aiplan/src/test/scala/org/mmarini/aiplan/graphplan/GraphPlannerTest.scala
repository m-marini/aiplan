package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class GraphPlannerTest extends FunSpec with Matchers {

  def fullPlan(planner: GraphPlanner): planner.PlanGraph = {
    def expand(plan: planner.PlanGraph): planner.PlanGraph = {
      val ep = planner.expandNextLayer(plan)
      if (ep.head.stateLayer.isSameLayer(ep(1).stateLayer))
        plan
      else
        expand(ep)
    }

    expand(planner.initialGraphPlan)
  }

  describe("Given a problem with just one operator") {

    val problem = new PlanProblemDSL {
      init("init")
      define { operator("op1") require ("init") assert ("goal") }
    }.apply

    val planner = new GraphPlanner(problem)

    describe("the full planner") {
      val fp = fullPlan(planner)

      it("should have 2 layers") {
        fp should have size 2
      }

      describe("the 1st state layer") {
        val l = fp(1).stateLayer
        it("should contain init") {
          l.state should contain("init")
        }
        it("should contain 1 props") {
          l.state should have size 1
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the 1st op layer") {
        val l = fp(1).opLayer
        val descr = l.ops.map(_.descr).filterNot(_ startsWith ("nop("))
        it("should contain no operators") {
          descr shouldBe empty
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the last state layer") {
        val l = fp.head.stateLayer
        it("should contain init,goal") {
          l.state should contain("init")
          l.state should contain("goal")
        }
        it("should contain 2 props") {
          l.state should have size 2
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the last op layer") {
        val l = fp.head.opLayer
        val descr = l.ops.map(_.descr).filterNot(_ startsWith ("nop("))
        it("should contain op1") {
          descr should contain("op1")
        }
        it("should contain 1 operator") {
          descr should have size 1
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
    }
  }

  describe("Given a problem with just one operator and mutex props") {

    val problem = new PlanProblemDSL {
      init("init")
      define { operator("op1") require ("init") assert ("prop1") deny ("init") }
      define { operator("op2") require ("init") assert ("prop2") deny ("init") }
    }.apply

    val planner = new GraphPlanner(problem)

    describe("the full planner") {
      val fp = fullPlan(planner)

      it("should have 2 layers") {
        fp should have size 2
      }

      describe("the 1st op layer") {
        val l = fp(1).opLayer
        it("should contain no operators") {
          l.ops shouldBe empty
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the 1st state layer") {
        val l = fp(1).stateLayer
        it("should contain init") {
          l.state should contain("init")
        }
        it("should contain 1 prop") {
          l.state should have size 1
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the 2nd op layer") {
        val l = fp(0).opLayer
        val descr = l.ops.map(_.descr)
        it("should contain nop(init),op1,op2") {
          descr should contain("nop(init)")
          descr should contain("op1")
          descr should contain("op2")
        }
        it("should contain 3 operators") {
          descr should have size 3
        }
        describe("the mutex descr") {
          val md = l.mutex.map { case (a, b) => (a.descr, b.descr) }

          it("should contain (nop(init),prop1), ... mutex") {
            md should contain("nop(init)", "op1")
            md should contain("nop(init)", "op2")
            md should contain("op1", "op2")
          }
        }
        it("should contain 6 mutex") {
          l.mutex should have size 6
        }
      }
    }
  }
}
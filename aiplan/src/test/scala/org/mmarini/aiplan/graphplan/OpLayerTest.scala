package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class OpLayerTest extends FunSpec with Matchers {

  describe("In problem of mutex ops") {
    /*
     * Two propositions are mutex if:
     * - there is no single operator in the layer that has both p1 and p2 as
     *   positive effects and
     * - every operator in the layer that has p1 as positive effect is mutex
     *   with every operator in the layer that has p2 as positive effect
*/
    val problem = new PlanProblemDSL {
      /*
       * p1 and p2 are not mutex because op1 asserts both propositions
       * p1 and p3 are mutex because no operator asserts both propositions and
       *   op1 that asserts p1 is mutex with op3 that assert p3
       * p2 and p3 are mutex because no operator asserts both propositions and
       *   op1 that asserts p2 is mutex with op3 that assert p3
       */
      define { operator("op1") assert ("p1") assert ("p2") }
      define { operator("op3") assert ("p3") }
    }.apply

    val op1 = problem.opsMap("op1")
    val op3 = problem.opsMap("op3")
    val nop1 = problem.opsMap("nop(p1)")
    val nop2 = problem.opsMap("nop(p2)")
    val nop3 = problem.opsMap("nop(p3)")

    val ol = OpLayer(new StateLayer(problem), Set(op1, op3), Set((op1, op3), (op3, op1)))

    describe("the next state layer") {
      val sl = ol.next

      it("should contain p1,p2,p3") {
        sl.state should contain("p1")
        sl.state should contain("p2")
        sl.state should contain("p3")
      }

      it("should have 3 props") {
        sl.state should have size 3
      }

      it("should contain (p1,p3),(p1,p2),...") {
        sl.mutex should contain("p1", "p3")
        sl.mutex should contain("p2", "p3")
        sl.mutex should contain("p3", "p1")
        sl.mutex should contain("p3", "p2")
      }
      it("should contain 4 mutex") {
        sl.mutex should have size 4
      }
    }
  }

  describe("When a planing problem with (a,b) String set and (nop,change) operator set") {
    val problem = new PlanProblemDSL {
      init("a")
      define { operator("change a to b") require ("a") assert ("b") deny ("a") }
      define { operator("change b to a") require ("b") assert ("a") deny ("b") }
    }.apply

    val changeab = problem.opsMap("change a to b")
    val changeba = problem.opsMap("change b to a")

    describe("the first action layer from (a)") {
      val l1 = new StateLayer(problem).next

      it("should contain nextProps {a, b}") {
        l1.nextProps should contain("a")
        l1.nextProps should contain("b")
      }
      it("should contain nextMutex {(a, b), (b, a)}") {
        l1.nextMutex should contain("a", "b")
        l1.nextMutex should contain("b", "a")
      }

      describe("the next state layer of first action layer from (a)") {
        val l2 = new StateLayer(problem).next.next
        it("should contain props {a, b}") {
          l2.state should contain("a")
          l2.state should contain("b")
        }
        it("should contain mutex {(a, b), (b, a)}") {
          l2.mutex should contain("a", "b")
          l2.mutex should contain("b", "a")
        }
      }
    }
  }
}
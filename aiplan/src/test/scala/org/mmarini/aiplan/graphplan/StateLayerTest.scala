package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class StateLayerTest extends FunSpec with Matchers {
  describe("When a planing problem with (a,b) proposition set and (nop,change) operator set") {

    val problem = new PlanProblemDSL {
      init("a")
      define { operator("change") require ("a") assert ("b") deny ("a") }
    }.apply

    val nopa = problem.opsMap("nop(a)")
    val change = problem.opsMap("change")

    describe("the next state layer of initial state (a)") {
      val l1 = new StateLayer(problem.init).next(problem.ops)
      it("should contain ops {nop(a), change}") {
        l1.ops should contain theSameElementsAs Set(nopa, change)
      }
      it("should contain mutex {(nop(a), change), (change, nop(a))}") {
        l1.mutex should contain theSameElementsAs Set((nopa, change), (change, nopa))
      }
    }
  }

  /*
   * Two operators are mutex if:
   * - they are dependent or
   * - a precondition of op1 is mutex with a precondition of op2
   * 
   * Two operators are indipendent iff:
   * - any negative effects of op1 are neither in requirements nor positive effects of op2 and
   * - any negative effects of op2 are neither in requirements and positive effects of op1
   * or in other word
   * - not exists any negative effects of op1 that is a precondition or a positive effect of op2 and
   * - not exists any negative effects of op2 that is a precondition or a positive effect of op1 and
   * 
   */
  describe("Mutex operator nop because dependent ops") {
    val problem = new PlanProblemDSL {
      /*
       * op1 is dependent by op2 because op1 deny p2 that is required by op1
       * op1 is dependent by op3 because op1 deny p2 that is asserted by op1
       * op2 is independent by op3
       * op1 is dependent by nop(op2) because op1 deny p2 that is required by nop(op2)
       */

      define(operator("op1") require ("p1") deny ("p2"))
      define(operator("op2") require ("p2") assert ("p3"))
      define(operator("op3") assert ("p2"))
    }.apply

    val op1 = problem.opsMap("op1")
    val op2 = problem.opsMap("op2")
    val op3 = problem.opsMap("op3")
    val nop1 = problem.opsMap("nop(p1)")
    val nop2 = problem.opsMap("nop(p2)")
    val nop3 = problem.opsMap("nop(p3)")

    it("op1 and op2 should not be independent") {
      op1 isIndipendent op2 shouldBe (false)
    }
    it("op1 and op3 should not be independent") {
      op1 isIndipendent op3 shouldBe (false)
    }
    it("op2 and op3 should be independent") {
      op2 isIndipendent op3 shouldBe (true)
    }

    describe("a state layer") {
      val sl = StateLayer(Set("p1", "p2"), Set())

      describe("the next operator layer") {
        val ol = sl.next(problem.ops)

        it("should contain op1,...") {
          val ids = ol.ops.map(_.descr)

          ids should contain("op1")
          ids should contain("op2")
          ids should contain("op3")
          ids should contain("nop(p1)")
          ids should contain("nop(p2)")
        }

        it("should contain 5 ops") {
          ol.ops should have size 5
        }

        it("should contains (op1,op2),(op1,op3)") {
          val ids = ol.mutex.map { case (a, b) => (a.descr, b.descr) }

          ids should contain("op1", "op2")
          ids should contain("op1", "nop(p2)")
          ids should contain("op1", "op3")
        }

        it("should contain 6 mutex") {
          ol.mutex should have size 6
        }
      }
    }
  }

  /*
   * Two operators are mutex if:
   * - they are dependent or
   * - a precondition of op1 is mutex with a precondition of op2
   * 
   * Two operators are indipendent iff:
   * - any negative effects of op1 are neither in requirements nor positive effects of op2 and
   * - any negative effects of op2 are neither in requirements and positive effects of op1
   * or in other word
   * - not exists any negative effects of op1 that is a precondition or a positive effect of op2 and
   * - not exists any negative effects of op2 that is a precondition or a positive effect of op1 and
   * 
   */
  describe("Mutex operator because of mutex preconditions") {
    val problem = new PlanProblemDSL {
      /*
       * op1 and op2 are mutex because p1 and p2 are mutex
       * op1 and nop(op2) are mutex because p1 and p2 are mutex
       * op2 and nop(op1) are mutex because p1 and p2 are mutex
       * nop(p1) and nop(op2) are mutex because p1 and p2 are mutex
       */

      define(operator("op1") require ("p1") assert ("p3"))
      define(operator("op2") require ("p2") assert ("p3"))
    }.apply

    val op1 = problem.opsMap("op1")
    val op2 = problem.opsMap("op2")
    val nop1 = problem.opsMap("nop(p1)")
    val nop2 = problem.opsMap("nop(p2)")
    val nop3 = problem.opsMap("nop(p3)")

    it("op1 and op2 should be independent") {
      op1 isIndipendent op2 shouldBe (true)
    }

    describe("a state layer with p1 and p2 mutex") {
      val sl = StateLayer(Set("p1", "p2"), Set(("p1", "p2"), ("p2", "p1")))

      describe("the next operator layer") {
        val ol = sl.next(problem.ops)

        it("should contain op1,...") {
          val ids = ol.ops.map(_.descr)

          ids should contain("op1")
          ids should contain("op2")
          ids should contain("nop(p1)")
          ids should contain("nop(p2)")
        }

        it("should contain 4 ops") {
          ol.ops should have size 4
        }

        it("should contains (op1,op2),(op1,nop(p2)),(op2,nop(p1),...") {
          val ids = ol.mutex.map { case (a, b) => (a.descr, b.descr) }

          ids should contain("op1", "op2")
          ids should contain("op1", "nop(p2)")
          ids should contain("op2", "nop(p1)")
          ids should contain("nop(p1)", "nop(p2)")
        }

        it("should contain 8 mutex") {
          ol.mutex should have size 8
        }
      }
    }
  }
}
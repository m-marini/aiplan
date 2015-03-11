package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class StateLayerTest extends FunSpec with Matchers {
  describe("When a planing problem with (a,b) proposition set and (nop,change) operator set") {

    val a: String = "a"
    val b: String = "b"
    val props = Set(a, b)

    def nop(p: String) = Operator(Set(p), Set(p))
    def change(from: String, to: String) = Operator(Set(from), Set(to), Set(from))

    def ops = (for (p <- props) yield nop(p)) ++
      (for {
        p1 <- props
        p2 <- props if (p1 != p2)
      } yield change(p1, p2))

    describe("the next state layer of initial state (a)") {
      val init = Set(a)
      val l1 = new StateLayer(init).next(ops)
      it("should contain ops {nop(a), change(a,b)}") {
        l1.ops should contain theSameElementsAs Set(nop(a), change(a, b))
      }
      it("should contain mutex {(nop(a), change(a,b)), (change(a,b), nop(a))}") {
        l1.mutex should contain theSameElementsAs Set((nop(a), change(a, b)), (change(a, b), nop(a)))
      }
    }
  }
}
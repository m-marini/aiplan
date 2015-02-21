package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class StateLayerTest extends FunSpec with Matchers {
  describe("When a planing problem with (a,b) proposition set and (nop,change) operator set") {

    val a: Proposition = "a"
    val b: Proposition = "b"
    val props = Set(a, b)

    def nop(p: Proposition) = Operator(Set(p), Set(p), Set())
    def change(from: Proposition, to: Proposition) = Operator(Set(from), Set(to), Set(from))

    def ops = (for (p <- props) yield nop(p)) ++
      (for {
        p1 <- props
        p2 <- props if (p1 != p2)
      } yield change(p1, p2))

    describe("the next state layer of initial state (a)") {
      val init = State(Set(a))
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
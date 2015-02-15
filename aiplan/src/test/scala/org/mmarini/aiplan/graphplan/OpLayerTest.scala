package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class OpLayerTest extends FunSpec with Matchers {
  describe("When a planing problem with (a,b) proposition set and (nop,change) operator set") {

    val a = Proposition("a")
    val b = Proposition("b")
    val props = Set(a, b)

    def nop(p: Proposition) = Operator(Set(p), Set(p), Set())
    def change(from: Proposition, to: Proposition) = Operator(Set(from), Set(to), Set(from))

    def ops = (for (p <- props) yield nop(p)) ++
      (for {
        p1 <- props
        p2 <- props if (p1 != p2)
      } yield change(p1, p2))

    describe("the first action layer from (a)") {
      val init = State(Set(a))
      val l1 = new StateLayer(init).next(ops)

      it("should contain nextProps {a, b}") {
        l1.nextProps should contain theSameElementsAs Set(a, b)
      }
      it("should contain mapPropOp{a, b}") {
        l1.mapPropOp should contain theSameElementsAs Map(a -> Set(nop(a)), b -> Set(change(a, b)))
      }
      it("should contain nextMutex {(a, b), (b, a)}") {
        l1.nextMutex should contain theSameElementsAs Set((a, b), (b, a))
      }

      describe("the next state layer of first action layer from (a)") {
        val l2 = new StateLayer(init).next(ops).next
        it("should contain props {a, b}") {
          l2.state.propositions should contain theSameElementsAs Set(a, b)
        }
        it("should contain mutex {(a, b), (b, a)}") {
          l2.mutex should contain theSameElementsAs Set((a, b), (b, a))
        }
      }
    }
  }
}
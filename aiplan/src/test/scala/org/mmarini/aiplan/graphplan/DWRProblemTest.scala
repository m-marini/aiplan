package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class DWRProblemTest extends FunSpec with Matchers {
  describe("Defined the DWR Problem") {
    import DWRProblem._

    describe("the initial state layer") {
      val l0 = new StateLayer(problem.init)

      it("should contain props {R.at(1), Q.at(2), A.at(1), B.at(2), R.unloaded, Q.unloaded}") {
        l0.state.propositions should contain theSameElementsAs Set(
          R.at(L1),
          Q.at(L2),
          A.at(L1),
          B.at(L2),
          R.unloaded,
          Q.unloaded)
      }

      describe("the first action layer") {
        val al1 = l0.next(ops)
        it("should contain ops {R.move(1,2), Q.move(2,1), R.load(A,1), Q.load(B,2), nop ...}") {
          al1.ops should contain theSameElementsAs Set(
            R.move(L1, L2),
            Q.move(L2, L1),
            R.load(A, L1),
            Q.load(B, L2),
            nop(R.at(L1)),
            nop(Q.at(L2)),
            nop(A.at(L1)),
            nop(B.at(L2)),
            nop(R.unloaded),
            nop(Q.unloaded))
        }
        it("should contain mutex with size 16") {
          al1.mutex should have size (16)
        }
        it("should contain mutex {R.move(1,2), R.load(A,1)}") {
          al1.mutex should contain(R.move(L1, L2), R.load(A, L1))
          al1.mutex should contain(R.load(A, L1), R.move(L1, L2))
        }
        it("should contain mutex {Q.move(2,1), Q.load(B,1)}") {
          al1.mutex should contain(Q.move(L2, L1), Q.load(B, L2))
          al1.mutex should contain(Q.load(B, L2), Q.move(L2, L1))
        }
        it("should contain mutex {R.move(1,2), nop(R.at(1)}") {
          al1.mutex should contain(R.move(L1, L2), nop(R.at(L1)))
          al1.mutex should contain(nop(R.at(L1)), R.move(L1, L2))
        }
        it("should contain mutex {Q.move(2,1), nop(Q.at(2)}") {
          al1.mutex should contain(Q.move(L2, L1), nop(Q.at(L2)))
          al1.mutex should contain(nop(Q.at(L2)), Q.move(L2, L1))
        }
        it("should contain mutex {R.load(A,1), nop(R.unloaded}") {
          al1.mutex should contain(R.load(A, L1), nop(R.unloaded))
          al1.mutex should contain(nop(R.unloaded), R.load(A, L1))
        }
        it("should contain mutex {Q.load(B,2), nop(Q.unloaded}") {
          al1.mutex should contain(Q.load(B, L2), nop(Q.unloaded))
          al1.mutex should contain(nop(Q.unloaded), Q.load(B, L2))
        }
        it("should contain mutex {R.load(A,1), nop(A.at(1))}") {
          al1.mutex should contain(R.load(A, L1), nop(A.at(L1)))
          al1.mutex should contain(nop(A.at(L1)), R.load(A, L1))
        }
        it("should contain mutex {Q.load(B,2), nop(B.at(2))}") {
          al1.mutex should contain(Q.load(B, L2), nop(B.at(L2)))
          al1.mutex should contain(nop(B.at(L2)), Q.load(B, L2))
        }

        describe("the first state layer") {
          val sl1 = al1.next

          it("should contain props {R.at(1), Q.at(2), A.at(1), B.at(2), R.unloaded, Q.unloaded, R.at(2), Q.at(1), A.on(R), B.on(Q)}") {
            sl1.state.propositions should contain theSameElementsAs Set(
              R.at(L1),
              R.at(L2),
              Q.at(L1),
              Q.at(L2),
              A.at(L1),
              A.on(R),
              B.at(L2),
              B.on(Q),
              R.unloaded,
              Q.unloaded)
          }
          it("should contain mutex with size 16") {
            sl1.mutex should have size (16)
          }
          it("should contain mutex {R.at(1), R.at(2)}") {
            sl1.mutex should contain(R.at(L1), R.at(L2))
          }
          it("should contain mutex {Q.at(1), Q.at(2)}") {
            sl1.mutex should contain(Q.at(L1), Q.at(L2))
          }
          it("should contain mutex {A.at(1), A.on(R)}") {
            sl1.mutex should contain(A.at(L1), A.on(R))
          }
          it("should contain mutex {B.at(2), A.on(Q)}") {
            sl1.mutex should contain(B.at(L2), B.on(Q))
          }
          it("should contain mutex {A.on(R), R.unloaded}") {
            sl1.mutex should contain(A.on(R), R.unloaded)
          }
          it("should contain mutex {B.on(Q), Q.unloaded}") {
            sl1.mutex should contain(B.on(Q), Q.unloaded)
          }
          it("should contain mutex {R.at(2), A.on(R)}") {
            sl1.mutex should contain(R.at(L2), A.on(R))
          }
          it("should contain mutex {Q.at(1), B.on(Q)}") {
            sl1.mutex should contain(Q.at(L1), B.on(Q))
          }

          describe("the second action layer") {
            val al2 = sl1.next(ops)
            it("should contain 20 ops") {
              al2.ops should have size (20)
            }
            it("should contain ops R.move(1,2) ...") {
              al2.ops should contain(R.move(L1, L2))
              al2.ops should contain(R.move(L2, L1))
              al2.ops should contain(Q.move(L2, L1))
              al2.ops should contain(Q.move(L1, L2))
              al2.ops should contain(R.load(A, L1))
              al2.ops should contain(R.load(B, L2))
              al2.ops should contain(Q.load(A, L1))
              al2.ops should contain(Q.load(B, L2))
              al2.ops should contain(R.unload(A, L1))
              al2.ops should contain(Q.unload(B, L2))
              al2.ops should contain(nop(R.at(L1)))
              al2.ops should contain(nop(R.at(L2)))
              al2.ops should contain(nop(Q.at(L1)))
              al2.ops should contain(nop(Q.at(L2)))
              al2.ops should contain(nop(R.unloaded))
              al2.ops should contain(nop(Q.unloaded))
              al2.ops should contain(nop(A.at(L1)))
              al2.ops should contain(nop(A.on(R)))
              al2.ops should contain(nop(B.at(L2)))
              al2.ops should contain(nop(B.on(Q)))
            }
            describe("the second proposition layer") {
              val sl2 = al2.next
              it("should contain 12 ops") {
                sl2.state.propositions should have size (12)
              }
              it("should contain ops R.move(1,2) ...") {
                sl2.state.propositions should contain(R.at(L1))
                sl2.state.propositions should contain(R.at(L2))
                sl2.state.propositions should contain(Q.at(L1))
                sl2.state.propositions should contain(Q.at(L2))
                sl2.state.propositions should contain(A.at(L1))
                sl2.state.propositions should contain(A.on(R))
                sl2.state.propositions should contain(A.on(Q))
                sl2.state.propositions should contain(B.at(L2))
                sl2.state.propositions should contain(B.on(Q))
                sl2.state.propositions should contain(B.on(R))
                sl2.state.propositions should contain(R.unloaded)
                sl2.state.propositions should contain(Q.unloaded)
              }
              describe("the third action layer") {
                val al3 = sl2.next(ops)
                it("should contain 14 ops") {
                  al3.ops should have size (26)
                }
                it("should contain ops R.move(1,2) ...") {
                  al3.ops should contain(R.move(L1, L2))
                  al3.ops should contain(R.move(L2, L1))
                  al3.ops should contain(Q.move(L2, L1))
                  al3.ops should contain(Q.move(L1, L2))
                  al3.ops should contain(R.load(A, L1))
                  al3.ops should contain(R.load(B, L2))
                  al3.ops should contain(Q.load(A, L1))
                  al3.ops should contain(Q.load(B, L2))
                  al3.ops should contain(R.unload(A, L1))
                  al3.ops should contain(Q.unload(B, L2))
                  al3.ops should contain(nop(R.at(L1)))
                  al3.ops should contain(nop(R.at(L2)))
                  al3.ops should contain(nop(Q.at(L1)))
                  al3.ops should contain(nop(Q.at(L2)))
                  al3.ops should contain(nop(R.unloaded))
                  al3.ops should contain(nop(Q.unloaded))
                  al3.ops should contain(nop(A.at(L1)))
                  al3.ops should contain(nop(A.on(R)))
                  al3.ops should contain(nop(B.at(L2)))
                  al3.ops should contain(nop(B.on(Q)))
                }
                describe("the second proposition layer") {
                  val sl3 = al3.next
                  it("should contain 14 ops") {
                    sl3.state.propositions should have size (14)
                  }
                  it("should contain ops R.move(1,2) ...") {
                    sl3.state.propositions should contain(R.at(L1))
                    sl3.state.propositions should contain(R.at(L2))
                    sl3.state.propositions should contain(Q.at(L1))
                    sl3.state.propositions should contain(Q.at(L2))
                    sl3.state.propositions should contain(A.at(L1))
                    sl3.state.propositions should contain(A.at(L2))
                    sl3.state.propositions should contain(A.on(R))
                    sl3.state.propositions should contain(A.on(Q))
                    sl3.state.propositions should contain(B.at(L1))
                    sl3.state.propositions should contain(B.at(L2))
                    sl3.state.propositions should contain(B.on(Q))
                    sl3.state.propositions should contain(B.on(R))
                    sl3.state.propositions should contain(R.unloaded)
                    sl3.state.propositions should contain(Q.unloaded)
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
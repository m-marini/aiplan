package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.dwr.DWRProblem

/**
 *
 */
class DWRProblemTest extends FunSpec with Matchers {
  import DWRProblem._

  val problem = DWRProblem.apply

  describe("the initial state layer of DWR problem") {
    val l0 = new StateLayer(problem.init)

    it("should contain props {R at L1, Q at L2, A at L1, B at L2, R unloaded, Q unloaded}") {
      l0.state should contain(isAt("R", "L1"))
      l0.state should contain(isAt("Q", "L2"))
      l0.state should contain(isAt("A", "L1"))
      l0.state should contain(isAt("B", "L2"))
      l0.state should contain(isUnloaded("R"))
      l0.state should contain(isUnloaded("Q"))
    }
  }

  describe("the GraphPlanner of DWR problem") {
    val planner = new GraphPlanner(problem)

    describe("the generated plan") {
      val pOpt = planner.plan
      it("should exists and have 3 size") {
        pOpt should not be empty
        pOpt.get should have size 3
      }

      describe("1st operator id set") {
        val o1 = pOpt.get(0).map(_.toString)
        it("should contains ...") {
          o1 should contain("Load A on R")
          o1 should contain("Load B on Q")
        }
      }

      describe("2nd operator id set") {
        val o1 = pOpt.get(1).map(_.toString)
        it("should contains ...") {
          o1 should contain("Move R to L2")
          o1 should contain("Move Q to L1")
        }
      }

      describe("3rd operator id set") {
        val o1 = pOpt.get(2).map(_.toString)
        it("should contains ...") {
          o1 should contain("Unload A from R")
          o1 should contain("Unload B from Q")
        }
      }

    }
  }
}
//
//      describe("the first action layer") {
//        val al1 = l0.next(problem.ops)
//        it("should contain ops {R.move(1,2), Q.move(2,1), R.load(A,1), Q.load(B,2), nop ...}") {
//          al1.ops should contain (
//            R.move(L1, L2),
//            Q.move(L2, L1),
//            R.load(A, L1),
//            Q.load(B, L2),
//            Operator(R.at(L1)),
//            Operator(Q.at(L2)),
//            Operator(A.at(L1)),
//            Operator(B.at(L2)),
//            Operator(R.unloaded),
//            Operator(Q.unloaded))
//        }
//        it("should contain mutex with size 16") {
//          al1.mutex should have size (16)
//        }
//        it("should contain mutex {R.move(1,2), R.load(A,1)}") {
//          al1.mutex should contain(R.move(L1, L2), R.load(A, L1))
//          al1.mutex should contain(R.load(A, L1), R.move(L1, L2))
//        }
//        it("should contain mutex {Q.move(2,1), Q.load(B,1)}") {
//          al1.mutex should contain(Q.move(L2, L1), Q.load(B, L2))
//          al1.mutex should contain(Q.load(B, L2), Q.move(L2, L1))
//        }
//        it("should contain mutex {R.move(1,2), nop(R.at(1)}") {
//          al1.mutex should contain(R.move(L1, L2), Operator(R.at(L1)))
//          al1.mutex should contain(Operator(R.at(L1)), R.move(L1, L2))
//        }
//        it("should contain mutex {Q.move(2,1), nop(Q.at(2)}") {
//          al1.mutex should contain(Q.move(L2, L1), Operator(Q.at(L2)))
//          al1.mutex should contain(Operator(Q.at(L2)), Q.move(L2, L1))
//        }
//        it("should contain mutex {R.load(A,1), nop(R.unloaded}") {
//          al1.mutex should contain(R.load(A, L1), Operator(R.unloaded))
//          al1.mutex should contain(Operator(R.unloaded), R.load(A, L1))
//        }
//        it("should contain mutex {Q.load(B,2), nop(Q.unloaded}") {
//          al1.mutex should contain(Q.load(B, L2), Operator(Q.unloaded))
//          al1.mutex should contain(Operator(Q.unloaded), Q.load(B, L2))
//        }
//        it("should contain mutex {R.load(A,1), nop(A.at(1))}") {
//          al1.mutex should contain(R.load(A, L1), Operator(A.at(L1)))
//          al1.mutex should contain(Operator(A.at(L1)), R.load(A, L1))
//        }
//        it("should contain mutex {Q.load(B,2), nop(B.at(2))}") {
//          al1.mutex should contain(Q.load(B, L2), Operator(B.at(L2)))
//          al1.mutex should contain(Operator(B.at(L2)), Q.load(B, L2))
//        }
//
//        describe("the first state layer") {
//          val sl1 = al1.next
//
//          it("should contain props {R.at(1), Q.at(2), A.at(1), B.at(2), R.unloaded, Q.unloaded, R.at(2), Q.at(1), A.on(R), B.on(Q)}") {
//            sl1.state should contain theSameElementsAs Set(
//              R.at(L1),
//              R.at(L2),
//              Q.at(L1),
//              Q.at(L2),
//              A.at(L1),
//              A.on(R),
//              B.at(L2),
//              B.on(Q),
//              R.unloaded,
//              Q.unloaded)
//          }
//          it("should contain mutex with size 16") {
//            sl1.mutex should have size (16)
//          }
//          it("should contain mutex {R.at(1), R.at(2)}") {
//            sl1.mutex should contain(R.at(L1), R.at(L2))
//          }
//          it("should contain mutex {Q.at(1), Q.at(2)}") {
//            sl1.mutex should contain(Q.at(L1), Q.at(L2))
//          }
//          it("should contain mutex {A.at(1), A.on(R)}") {
//            sl1.mutex should contain(A.at(L1), A.on(R))
//          }
//          it("should contain mutex {B.at(2), A.on(Q)}") {
//            sl1.mutex should contain(B.at(L2), B.on(Q))
//          }
//          it("should contain mutex {A.on(R), R.unloaded}") {
//            sl1.mutex should contain(A.on(R), R.unloaded)
//          }
//          it("should contain mutex {B.on(Q), Q.unloaded}") {
//            sl1.mutex should contain(B.on(Q), Q.unloaded)
//          }
//          it("should contain mutex {R.at(2), A.on(R)}") {
//            sl1.mutex should contain(R.at(L2), A.on(R))
//          }
//          it("should contain mutex {Q.at(1), B.on(Q)}") {
//            sl1.mutex should contain(Q.at(L1), B.on(Q))
//          }
//
//          describe("the second action layer") {
//            val al2 = sl1.next(ops)
//            it("should contain 20 ops") {
//              al2.ops should have size (20)
//            }
//            it("should contain ops R.move(1,2) ...") {
//              al2.ops should contain(R.move(L1, L2))
//              al2.ops should contain(R.move(L2, L1))
//              al2.ops should contain(Q.move(L2, L1))
//              al2.ops should contain(Q.move(L1, L2))
//              al2.ops should contain(R.load(A, L1))
//              al2.ops should contain(R.load(B, L2))
//              al2.ops should contain(Q.load(A, L1))
//              al2.ops should contain(Q.load(B, L2))
//              al2.ops should contain(R.unload(A, L1))
//              al2.ops should contain(Q.unload(B, L2))
//              al2.ops should contain(Operator(R.at(L1)))
//              al2.ops should contain(Operator(R.at(L2)))
//              al2.ops should contain(Operator(Q.at(L1)))
//              al2.ops should contain(Operator(Q.at(L2)))
//              al2.ops should contain(Operator(R.unloaded))
//              al2.ops should contain(Operator(Q.unloaded))
//              al2.ops should contain(Operator(A.at(L1)))
//              al2.ops should contain(Operator(A.on(R)))
//              al2.ops should contain(Operator(B.at(L2)))
//              al2.ops should contain(Operator(B.on(Q)))
//            }
//            describe("the second proposition layer") {
//              val sl2 = al2.next
//              it("should contain 12 ops") {
//                sl2.state should have size (12)
//              }
//              it("should contain ops R.move(1,2) ...") {
//                sl2.state should contain(R.at(L1))
//                sl2.state should contain(R.at(L2))
//                sl2.state should contain(Q.at(L1))
//                sl2.state should contain(Q.at(L2))
//                sl2.state should contain(A.at(L1))
//                sl2.state should contain(A.on(R))
//                sl2.state should contain(A.on(Q))
//                sl2.state should contain(B.at(L2))
//                sl2.state should contain(B.on(Q))
//                sl2.state should contain(B.on(R))
//                sl2.state should contain(R.unloaded)
//                sl2.state should contain(Q.unloaded)
//              }
//              describe("the third action layer") {
//                val al3 = sl2.next(ops)
//                it("should contain 14 ops") {
//                  al3.ops should have size (26)
//                }
//                it("should contain ops R.move(1,2) ...") {
//                  al3.ops should contain(R.move(L1, L2))
//                  al3.ops should contain(R.move(L2, L1))
//                  al3.ops should contain(Q.move(L2, L1))
//                  al3.ops should contain(Q.move(L1, L2))
//                  al3.ops should contain(R.load(A, L1))
//                  al3.ops should contain(R.load(B, L2))
//                  al3.ops should contain(Q.load(A, L1))
//                  al3.ops should contain(Q.load(B, L2))
//                  al3.ops should contain(R.unload(A, L1))
//                  al3.ops should contain(Q.unload(B, L2))
//                  al3.ops should contain(Operator(R.at(L1)))
//                  al3.ops should contain(Operator(R.at(L2)))
//                  al3.ops should contain(Operator(Q.at(L1)))
//                  al3.ops should contain(Operator(Q.at(L2)))
//                  al3.ops should contain(Operator(R.unloaded))
//                  al3.ops should contain(Operator(Q.unloaded))
//                  al3.ops should contain(Operator(A.at(L1)))
//                  al3.ops should contain(Operator(A.on(R)))
//                  al3.ops should contain(Operator(B.at(L2)))
//                  al3.ops should contain(Operator(B.on(Q)))
//                }
//                describe("the second proposition layer") {
//                  val sl3 = al3.next
//                  it("should contain 14 ops") {
//                    sl3.state should have size (14)
//                  }
//                  it("should contain ops R.move(1,2) ...") {
//                    sl3.state should contain(R.at(L1))
//                    sl3.state should contain(R.at(L2))
//                    sl3.state should contain(Q.at(L1))
//                    sl3.state should contain(Q.at(L2))
//                    sl3.state should contain(A.at(L1))
//                    sl3.state should contain(A.at(L2))
//                    sl3.state should contain(A.on(R))
//                    sl3.state should contain(A.on(Q))
//                    sl3.state should contain(B.at(L1))
//                    sl3.state should contain(B.at(L2))
//                    sl3.state should contain(B.on(Q))
//                    sl3.state should contain(B.on(R))
//                    sl3.state should contain(R.unloaded)
//                    sl3.state should contain(Q.unloaded)
//                  }
//                }
//              }
//            }
//          }
//        }
//      }
//    }
//  }
//}
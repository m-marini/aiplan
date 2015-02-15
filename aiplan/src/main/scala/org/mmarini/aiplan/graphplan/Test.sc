package org.mmarini.aiplan.graphplan

object Test {
  val l0 = new StateLayer(DWRProblem.problem.init)//> l0  : org.mmarini.aiplan.graphplan.StateLayer = StateLayer((R.at(1) B.at(2) 
                                                  //| R.unloaded Q.at(2) A.at(1) Q.unloaded),Set())

  val l2 = l0.next(DWRProblem.ops).next           //> l2  : org.mmarini.aiplan.graphplan.StateLayer = StateLayer((R.at(1) Q.at(1) 
                                                  //| B.at(2) B.on(Q) R.unloaded R.at(2) Q.at(2) A.on(R) A.at(1) Q.unloaded),Set((
                                                  //| Q.at(1),B.on(Q)), (R.unloaded,A.on(R)), (A.on(R),A.at(1)), (B.on(Q),Q.unload
                                                  //| ed), (Q.at(1),Q.at(2)), (R.at(2),A.on(R)), (Q.unloaded,B.on(Q)), (A.at(1),A.
                                                  //| on(R)), (B.on(Q),B.at(2)), (R.at(1),R.at(2)), (R.at(2),R.at(1)), (B.on(Q),Q.
                                                  //| at(1)), (Q.at(2),Q.at(1)), (A.on(R),R.unloaded), (B.at(2),B.on(Q)), (A.on(R)
                                                  //| ,R.at(2))))
  (l2.state.propositions.size, l2.mutex.size)     //> res0: (Int, Int) = (10,16)
  l2.state.propositions.mkString("\n")            //> res1: String = R.at(1)
                                                  //| Q.at(1)
                                                  //| B.at(2)
                                                  //| B.on(Q)
                                                  //| R.unloaded
                                                  //| R.at(2)
                                                  //| Q.at(2)
                                                  //| A.on(R)
                                                  //| A.at(1)
                                                  //| Q.unloaded

  val l4 = l2.next(DWRProblem.ops).next           //> l4  : org.mmarini.aiplan.graphplan.StateLayer = StateLayer((A.on(Q) R.at(1) 
                                                  //| Q.at(1) B.at(2) B.on(Q) R.unloaded R.at(2) Q.at(2) A.on(R) B.on(R) A.at(1) Q
                                                  //| .unloaded),Set((Q.at(1),B.on(Q)), (R.unloaded,A.on(R)), (R.unloaded,A.on(Q))
                                                  //| , (A.on(R),A.at(1)), (B.on(R),A.on(R)), (R.at(1),R.unloaded), (R.unloaded,R.
                                                  //| at(2)), (B.on(Q),Q.unloaded), (Q.at(2),B.at(2)), (Q.unloaded,B.on(R)), (R.at
                                                  //| (2),R.unloaded), (Q.at(1),B.at(2)), (A.on(Q),Q.unloaded), (R.unloaded,A.at(1
                                                  //| )), (Q.at(1),Q.at(2)), (A.on(Q),A.at(1)), (A.on(Q),B.on(Q)), (R.at(2),A.on(R
                                                  //| )), (Q.unloaded,B.on(Q)), (B.on(Q),B.on(R)), (A.on(Q),Q.at(1)), (A.at(1),A.o
                                                  //| n(R)), (R.at(1),B.on(R)), (R.at(2),B.on(R)), (B.at(2),A.on(Q)), (Q.at(2),A.o
                                                  //| n(Q)), (B.on(Q),A.on(Q)), (B.on(R),B.on(Q)), (Q.at(1),Q.unloaded), (B.at(2),
                                                  //| B.on(R)), (B.on(Q),B.at(2)), (B.on(R),B.at(2)), (Q.at(2),Q.unloaded), (A.on(
                                                  //| Q),R.unloaded), (R.at(1),R.at(2)), (Q.unloaded,Q.at(2)), (Q.unloaded,B.at(2)
                                                  //| ), (A.at(1),R.at(1)), (A
                                                  //| Output exceeds cutoff limit.
  (l4.state.propositions.size, l4.mutex.size)     //> res2: (Int, Int) = (12,72)
  l4.state.propositions.mkString("\n")            //> res3: String = A.on(Q)
                                                  //| R.at(1)
                                                  //| Q.at(1)
                                                  //| B.at(2)
                                                  //| B.on(Q)
                                                  //| R.unloaded
                                                  //| R.at(2)
                                                  //| Q.at(2)
                                                  //| A.on(R)
                                                  //| B.on(R)
                                                  //| A.at(1)
                                                  //| Q.unloaded
  val l6 = l4.next(DWRProblem.ops).next           //> l6  : org.mmarini.aiplan.graphplan.StateLayer = StateLayer((A.on(Q) R.at(1) 
                                                  //| Q.at(1) B.at(2) B.on(Q) R.unloaded R.at(2) Q.at(2) A.on(R) B.on(R) A.at(1) Q
                                                  //| .unloaded),Set((Q.at(1),B.on(Q)), (R.unloaded,A.on(R)), (R.unloaded,A.on(Q))
                                                  //| , (A.on(R),A.at(1)), (B.on(R),A.on(R)), (R.at(1),R.unloaded), (R.unloaded,R.
                                                  //| at(2)), (B.on(Q),Q.unloaded), (Q.at(2),B.at(2)), (Q.unloaded,B.on(R)), (R.at
                                                  //| (2),R.unloaded), (Q.at(1),B.at(2)), (A.on(Q),Q.unloaded), (R.unloaded,A.at(1
                                                  //| )), (Q.at(1),Q.at(2)), (A.on(Q),A.at(1)), (A.on(Q),B.on(Q)), (R.at(2),A.on(R
                                                  //| )), (Q.unloaded,B.on(Q)), (B.on(Q),B.on(R)), (A.on(Q),Q.at(1)), (A.at(1),A.o
                                                  //| n(R)), (R.at(1),B.on(R)), (R.at(2),B.on(R)), (B.at(2),A.on(Q)), (Q.at(2),A.o
                                                  //| n(Q)), (B.on(Q),A.on(Q)), (B.on(R),B.on(Q)), (Q.at(1),Q.unloaded), (B.at(2),
                                                  //| B.on(R)), (B.on(Q),B.at(2)), (B.on(R),B.at(2)), (Q.at(2),Q.unloaded), (A.on(
                                                  //| Q),R.unloaded), (R.at(1),R.at(2)), (Q.unloaded,Q.at(2)), (Q.unloaded,B.at(2)
                                                  //| ), (A.at(1),R.at(1)), (A
                                                  //| Output exceeds cutoff limit.
  (l6.state.propositions.size, l6.mutex.size)     //> res4: (Int, Int) = (12,72)
  l6.state.propositions.mkString("\n")            //> res5: String = A.on(Q)
                                                  //| R.at(1)
                                                  //| Q.at(1)
                                                  //| B.at(2)
                                                  //| B.on(Q)
                                                  //| R.unloaded
                                                  //| R.at(2)
                                                  //| Q.at(2)
                                                  //| A.on(R)
                                                  //| B.on(R)
                                                  //| A.at(1)
                                                  //| Q.unloaded
}
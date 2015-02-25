package org.mmarini.aiplan.graphplan

object Test {
  val p = new FFPlanner(DWRProblem.problem)       //> p  : org.mmarini.aiplan.graphplan.FFPlanner = org.mmarini.aiplan.graphplan.FF
                                                  //| Planner@5799182a

  val rpg = p.expandRPG(List((DWRProblem.problem.init, Set())))
                                                  //> rpg  : org.mmarini.aiplan.graphplan.Test.p.GraphPlan = List((Set(A.on(Q), R.
                                                  //| at(1), Q.at(1), B.at(2), B.on(Q), R.unloaded, A.at(2), R.at(2), Q.at(2), B.a
                                                  //| t(1), A.on(R), B.on(R), A.at(1), Q.unloaded),Set((+{A.on(Q)}, -{A.at(1) Q.un
                                                  //| loaded}, ?{Q.at(1) A.at(1) Q.unloaded}), (+{Q.at(1)}, -{}, ?{Q.at(1)}), (+{B
                                                  //| .on(Q)}, -{B.at(2) Q.unloaded}, ?{Q.at(2) B.at(2) Q.unloaded}), (+{B.at(2)},
                                                  //|  -{}, ?{B.at(2)}), (+{A.on(R)}, -{}, ?{A.on(R)}), (+{Q.at(2)}, -{}, ?{Q.at(2
                                                  //| )}), (+{B.on(R)}, -{B.at(2) R.unloaded}, ?{R.at(2) B.at(2) R.unloaded}), (+{
                                                  //| R.unloaded}, -{}, ?{R.unloaded}), (+{Q.unloaded}, -{}, ?{Q.unloaded}), (+{R.
                                                  //| at(1)}, -{R.at(2)}, ?{R.at(2)}), (+{A.on(R)}, -{A.at(1) R.unloaded}, ?{R.at(
                                                  //| 1) A.at(1) R.unloaded}), (+{Q.at(1)}, -{Q.at(2)}, ?{Q.at(2)}), (+{B.at(2) Q.
                                                  //| unloaded}, -{B.on(Q)}, ?{Q.at(2) B.on(Q)}), (+{A.at(2) R.unloaded}, -{A.on(R
                                                  //| )}, ?{R.at(2) A.on(R)}), (+{R.at(1)}, -{}, ?{R.at(1)}), (+{B.on(Q)}, -{}, ?{
                                                  //| B.on(Q)}), (+{B.at(1) Q.
                                                  //| Output exceeds cutoff limit.
  val fog = p.computeFOG(rpg)                     //> fog  : org.mmarini.aiplan.graphplan.Test.p.GraphPlan = List((Set(A.on(Q), A.
                                                  //| at(2), B.at(1), B.on(R)),Set((+{A.on(Q)}, -{A.at(1) Q.unloaded}, ?{Q.at(1) A
                                                  //| .at(1) Q.unloaded}), (+{Q.at(1)}, -{}, ?{Q.at(1)}), (+{A.on(R)}, -{}, ?{A.on
                                                  //| (R)}), (+{B.on(R)}, -{B.at(2) R.unloaded}, ?{R.at(2) B.at(2) R.unloaded}), (
                                                  //| +{R.at(1)}, -{R.at(2)}, ?{R.at(2)}), (+{B.at(2) Q.unloaded}, -{B.on(Q)}, ?{Q
                                                  //| .at(2) B.on(Q)}), (+{A.at(2) R.unloaded}, -{A.on(R)}, ?{R.at(2) A.on(R)}), (
                                                  //| +{B.on(Q)}, -{}, ?{B.on(Q)}), (+{B.at(1) Q.unloaded}, -{B.on(Q)}, ?{Q.at(1) 
                                                  //| B.on(Q)}), (+{A.at(1) R.unloaded}, -{A.on(R)}, ?{R.at(1) A.on(R)}), (+{R.at(
                                                  //| 2)}, -{}, ?{R.at(2)}), (+{Q.at(2)}, -{Q.at(1)}, ?{Q.at(1)}))), (Set(Q.at(1),
                                                  //|  B.on(Q), R.at(2), A.on(R)),Set((+{B.on(Q)}, -{B.at(2) Q.unloaded}, ?{Q.at(2
                                                  //| ) B.at(2) Q.unloaded}), (+{B.at(2)}, -{}, ?{B.at(2)}), (+{Q.at(2)}, -{}, ?{Q
                                                  //| .at(2)}), (+{R.unloaded}, -{}, ?{R.unloaded}), (+{Q.unloaded}, -{}, ?{Q.unlo
                                                  //| aded}), (+{A.on(R)}, -{A
                                                  //| Output exceeds cutoff limit.
  fog.map(_._1).mkString("\n")                    //> res0: String = Set(A.on(Q), A.at(2), B.at(1), B.on(R))
                                                  //| Set(Q.at(1), B.on(Q), R.at(2), A.on(R))
                                                  //| Set(R.at(1), B.at(2), R.unloaded, Q.at(2), A.at(1), Q.unloaded)
}
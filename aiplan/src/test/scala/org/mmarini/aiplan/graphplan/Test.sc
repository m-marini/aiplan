package org.mmarini.aiplan.graphplan

object Test {
  val p = new AStarPlanner(DWRProblem.problem, Heuristics.h0)
                                                  //> p  : org.mmarini.aiplan.graphplan.AStarPlanner = org.mmarini.aiplan.graphpla
                                                  //| n.AStarPlanner@2fa4a715
  val plan = p.plan                               //> 23:32:44.629 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner - exploring iter
                                                  //| ation   = 0
                                                  //| 23:32:44.631 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           frin
                                                  //| ge size = 1
                                                  //| 23:32:44.632 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           g   
                                                  //|         = 0,00000
                                                  //| 23:32:44.633 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           cost
                                                  //|         = 0,00000
                                                  //| 23:32:44.640 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner - exploring iter
                                                  //| ation   = 1
                                                  //| 23:32:44.641 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           frin
                                                  //| ge size = 4
                                                  //| 23:32:44.641 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           g   
                                                  //|         = 1,00000
                                                  //| 23:32:44.641 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           cost
                                                  //|         = 1,00000
                                                  //| 23:32:44.648 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner - exploring iter
                                                  //| ation   = 2
                                                  //| 23:32:44.648 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           frin
                                                  //| ge size = 6
                                                  //| 23
                                                  //| Output exceeds cutoff limit.

  plan.get.mkString("\n")                         //> res0: String = (+{A.on(R)}, -{A.at(1) R.unloaded}, ?{R.at(1) A.at(1) R.unloa
                                                  //| ded})
                                                  //| (+{R.at(2)}, -{R.at(1)}, ?{R.at(1)})
                                                  //| (+{A.at(2) R.unloaded}, -{A.on(R)}, ?{R.at(2) A.on(R)})
                                                  //| (+{B.on(R)}, -{B.at(2) R.unloaded}, ?{R.at(2) B.at(2) R.unloaded})
                                                  //| (+{R.at(1)}, -{R.at(2)}, ?{R.at(2)})
                                                  //| (+{B.at(1) R.unloaded}, -{B.on(R)}, ?{R.at(1) B.on(R)})
  val p1 = new GraphPlanner(DWRProblem.problem)   //> p1  : org.mmarini.aiplan.graphplan.GraphPlanner = org.mmarini.aiplan.graphpl
                                                  //| an.GraphPlanner@737f7f6
  val plan1 = p1.plan.get                         //> 23:32:44.752 [main] DEBUG o.m.aiplan.graphplan.StateLayer - 
                                                  //| 23:32:44.878 [main] DEBUG o.m.aiplan.graphplan.StateLayer - 
                                                  //| 23:32:44.879 [main] DEBUG o.m.aiplan.graphplan.StateLayer - Filtered (+{A.at
                                                  //| (2) R.unloaded}, -{A.on(R)}, ?{R.at(2) A.on(R)})
                                                  //| 23:32:44.879 [main] DEBUG o.m.aiplan.graphplan.StateLayer -  because (R.at(2
                                                  //| ),A.on(R))
                                                  //| 23:32:44.879 [main] DEBUG o.m.aiplan.graphplan.StateLayer - Filtered (+{B.at
                                                  //| (1) Q.unloaded}, -{B.on(Q)}, ?{Q.at(1) B.on(Q)})
                                                  //| 23:32:44.879 [main] DEBUG o.m.aiplan.graphplan.StateLayer -  because (Q.at(1
                                                  //| ),B.on(Q))
                                                  //| 23:32:45.007 [main] DEBUG o.m.aiplan.graphplan.StateLayer - 
                                                  //| 23:32:45.007 [main] DEBUG o.m.aiplan.graphplan.StateLayer - Filtered (+{A.at
                                                  //| (2) Q.unloaded}, -{A.on(Q)}, ?{Q.at(2) A.on(Q)})
                                                  //| 23:32:45.008 [main] DEBUG o.m.aiplan.graphplan.StateLayer -  because (Q.at(2
                                                  //| ),A.on(Q))
                                                  //| 23:32:45.008 [main] DEBUG o.m.aiplan.graphplan.StateLayer - Filtered (+{B.at
                                                  //| (1) R.unloaded}, -{B.on(R)}, ?
                                                  //| Output exceeds cutoff limit.
  plan1.mkString("\n")                            //> res1: String = Set((+{A.on(R)}, -{A.at(1) R.unloaded}, ?{R.at(1) A.at(1) R.u
                                                  //| nloaded}), (+{B.on(Q)}, -{B.at(2) Q.unloaded}, ?{Q.at(2) B.at(2) Q.unloaded}
                                                  //| ))
                                                  //| Set((+{R.at(2)}, -{R.at(1)}, ?{R.at(1)}), (+{Q.at(1)}, -{Q.at(2)}, ?{Q.at(2)
                                                  //| }))
                                                  //| Set((+{A.at(2) R.unloaded}, -{A.on(R)}, ?{R.at(2) A.on(R)}), (+{B.at(1) Q.un
                                                  //| loaded}, -{B.on(Q)}, ?{Q.at(1) B.on(Q)}))
plan1.flatten.mkString("\n")                      //> res2: String = (+{A.on(R)}, -{A.at(1) R.unloaded}, ?{R.at(1) A.at(1) R.unloa
                                                  //| ded})
                                                  //| (+{B.on(Q)}, -{B.at(2) Q.unloaded}, ?{Q.at(2) B.at(2) Q.unloaded})
                                                  //| (+{R.at(2)}, -{R.at(1)}, ?{R.at(1)})
                                                  //| (+{Q.at(1)}, -{Q.at(2)}, ?{Q.at(2)})
                                                  //| (+{A.at(2) R.unloaded}, -{A.on(R)}, ?{R.at(2) A.on(R)})
                                                  //| (+{B.at(1) Q.unloaded}, -{B.on(Q)}, ?{Q.at(1) B.on(Q)})
plan1.flatten.map(_.cost ).sum                    //> res3: Double = 6.0
plan1.flatten.size                                //> res4: Int = 6
}
package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.GraphPlanner
import YAAAFAProblem._
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics

object Test {
  val plan = new GraphPlanner(problem).plan       //> 23:06:59.895 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - backwardSearch
                                                  //|  for AFA tested
                                                  //| 23:06:59.901 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -               
                                                  //|  in XAS is built
                                                  //| XAS is deployed in TestEnv
                                                  //| AFA is deployed in TestEnv
                                                  //| XPI is built
                                                  //| TestEnv ready
                                                  //| AFA tested
                                                  //| AFA offloaded
                                                  //| PRE is built
                                                  //| XPI is deployed in TestEnv
                                                  //| IFG is deployed in TestEnv
                                                  //| UTL is deployed in TestEnv
                                                  //| UTL is built
                                                  //| PRE is deployed in TestEnv
                                                  //| IFG is built
                                                  //| AFA is built
                                                  //| 23:06:59.902 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - gpSearch goal=
                                                  //| {AFA tested}
                                                  //| 23:06:59.902 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          curre
                                                  //| nt operation set={}
                                                  //| 23:06:59.902 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          Selec
                                                  //| ted prop AFA tested
                                                  //| 23:06:59.903 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          prefi
                                                  //| ltered operation set={test AFA}
                                                  //| 23:06:59.903 [main] DEBUG o.m.aiplan.graphplan
                                                  //| Output exceeds cutoff limit.
  plan.get.mkString("\n")                         //> res0: String = Set(setUp TestEnv, build XAS, offLoad AFA, build UTL, build I
                                                  //| FG)
                                                  //| Set(setUp TestEnv, build XPI, build AFA, build PRE, deploy XAS in TestEnv)
                                                  //| Set(deploy UTL in TestEnv, deploy IFG in TestEnv, deploy PRE in TestEnv, dep
                                                  //| loy XPI in TestEnv, deploy AFA in TestEnv)
                                                  //| Set(test AFA)

  val plan1 = new AStarPlanner(problem, Heuristics.hff).plan
                                                  //> 23:06:59.952 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner - exploring iter
                                                  //| ation   = 0
                                                  //| 23:06:59.952 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           frin
                                                  //| ge size = 1
                                                  //| 23:06:59.954 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           g   
                                                  //|         = 0,00000
                                                  //| 23:06:59.971 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           cost
                                                  //|         = 1,00000
                                                  //| 23:06:59.989 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner - exploring iter
                                                  //| ation   = 1
                                                  //| 23:06:59.990 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           frin
                                                  //| ge size = 7
                                                  //| 23:06:59.990 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           g   
                                                  //|         = 1,00000
                                                  //| 23:06:59.993 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           cost
                                                  //|         = 2,00000
                                                  //| 23:07:00.018 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner - exploring iter
                                                  //| ation   = 2
                                                  //| 23:07:00.018 [main] DEBUG o.m.aiplan.graphplan.AStarPlanner -           frin
                                                  //| ge size = 12
                                                  //| 2
                                                  //| Output exceeds cutoff limit.
  plan1.get.mkString("\n")                        //> res1: String = setUp TestEnv
                                                  //| offLoad AFA
                                                  //| build AFA
                                                  //| deploy AFA in TestEnv
                                                  //| build UTL
                                                  //| deploy UTL in TestEnv
                                                  //| build XPI
                                                  //| build IFG
                                                  //| build PRE
                                                  //| deploy XPI in TestEnv
                                                  //| deploy PRE in TestEnv
                                                  //| deploy IFG in TestEnv
                                                  //| build XAS
                                                  //| deploy XAS in TestEnv
                                                  //| test AFA

}
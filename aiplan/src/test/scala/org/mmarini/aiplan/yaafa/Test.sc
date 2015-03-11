package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.GraphPlanner
import YAAAFAProblem._
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics
import org.mmarini.aiplan.graphplan.PlanProblemDSL

object Test {
  val plan = new GraphPlanner(problem).plan       //> 08:18:48.285 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - backwardSearch
                                                  //|  for Performance test has passed User acceptance test has passed
                                                  //| 08:18:48.290 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -               
                                                  //|  in Performance test has passed
                                                  //| User acceptance test has passed
                                                  //| 08:18:48.290 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - gpSearch goal=
                                                  //| {Performance test has passed User acceptance test has passed}
                                                  //| 08:18:48.290 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          curre
                                                  //| nt operation set={}
                                                  //| 08:18:48.291 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          Selec
                                                  //| ted prop Performance test has passed
                                                  //| 08:18:48.299 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          prefi
                                                  //| ltered operation set={Run performance test}
                                                  //| 08:18:48.299 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          filte
                                                  //| red operation set={Run performance test}
                                                  //| 08:18:48.328 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -   
                                                  //| Output exceeds cutoff limit.
  plan.get.mkString("\n")                         //> res0: String = Set(Run performance test, Run user acceptance test)
}
package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.GraphPlanner
import YAAAFAProblem._
import org.mmarini.aiplan.graphplan.AStarPlanner
import org.mmarini.aiplan.graphplan.Heuristics

object Test {
  val plan = new GraphPlanner(problem).plan       //> 23:42:51.999 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - backwardSearch
                                                  //|  for All tests have passed
                                                  //| 23:42:52.007 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -               
                                                  //|  in Functional test has passed
                                                  //| User acceptance test has passed
                                                  //| All tests have passed
                                                  //| Performance test has passed
                                                  //| 23:42:52.007 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - gpSearch goal=
                                                  //| {All tests have passed}
                                                  //| 23:42:52.008 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          curre
                                                  //| nt operation set={}
                                                  //| 23:42:52.008 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          Selec
                                                  //| ted prop All tests have passed
                                                  //| 23:42:52.010 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          prefi
                                                  //| ltered operation set={final check for tests}
                                                  //| 23:42:52.011 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          filte
                                                  //| red operation set={final check for tests}
                                                  //| 23:42:52.019 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          Selec
                                                  //| ted op
                                                  //| Output exceeds cutoff limit.
  plan.get.mkString("\n")                         //> res0: String = Set(run functional test)
                                                  //| Set(run performance test, run user acceptance test)
                                                  //| Set(final check for tests)
}
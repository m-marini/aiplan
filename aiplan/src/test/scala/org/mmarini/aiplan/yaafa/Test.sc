package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.GraphPlanner

object Test {
    val plan = new GraphPlanner(YAAAFAProblem.problem).plan
                                                  //> 18:16:52.940 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - backwardSearch
                                                  //|  for Performance test has passed User acceptance test has passed
                                                  //| 18:16:52.948 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -               
                                                  //|  in Functional test has passed
                                                  //| PRE has been built
                                                  //| PRE has been deployed
                                                  //| IFG has been deployed
                                                  //| User acceptance test has passed
                                                  //| XPS has been deployed
                                                  //| XAS has been deployed
                                                  //| AFA has been built
                                                  //| yaafa has been built
                                                  //| XPI has been built
                                                  //| yaafa has been deployed
                                                  //| XPI has been deployed
                                                  //| XAS has been built
                                                  //| IFG has been built
                                                  //| AFA has been deployed
                                                  //| yaafa has been developed
                                                  //| XPS has been built
                                                  //| AFA has been adapted
                                                  //| Performance test has passed
                                                  //| 18:16:52.949 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner - gpSearch goal=
                                                  //| {Performance test has passed User acceptance test has passed}
                                                  //| 18:16:52.950 [main] DEBUG o.m.aiplan.graphplan.GraphPlanner -          curre
                                                  //| nt operation set={}
                                                  //| 18:16:52.
                                                  //| Output exceeds cutoff limit.

    plan.get.mkString("\n")                       //> res0: String = Set(Built PRE, Adapt AFA, Built IFG, Develope yaafa, Built XP
                                                  //| I, Built XPS, Built XAS)
                                                  //| Set(Built AFA, Deploy XAS, Deploy XPI, Deploy XPS, Build yaafa, Deploy IFG, 
                                                  //| Deploy PRE)
                                                  //| Set(Deploy yaafa, Deploy AFA)
                                                  //| Set(Run functional test)
                                                  //| Set(Run performance test, Run user acceptance test)
}
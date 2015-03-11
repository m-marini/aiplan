package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.Proposition
import org.mmarini.aiplan.graphplan.PlanProblem
import org.mmarini.aiplan.graphplan.StringProposition
import org.mmarini.aiplan.graphplan.Operator
import org.mmarini.aiplan.graphplan.State
import org.mmarini.aiplan.graphplan.PlanProblemDSL

/**
 *
 */
object YAAAFAProblem extends PlanProblemDSL {
  val perftest = "Performance test has passed"
  val uat = "User acceptance test has passed"

  goal(perftest, uat)

  define { op("Run performance test") + perftest }
  define { op("Run user acceptance test") + uat }
}
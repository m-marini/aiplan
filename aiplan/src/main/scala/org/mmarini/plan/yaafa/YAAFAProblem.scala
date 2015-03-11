package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.PlanProblemDSL

/**
 *
 */
object YAAAFAProblem extends PlanProblemDSL {
  val perfTest = "Performance test has passed"
  val uat = "User acceptance test has passed"
  val funcTest = "Functional test has passed"

  val dependencies = "IFG PRE XPI XAS XPS".split("""\s""").toSet

  val deployed = dependencies.map(m => m -> s"$m has been deployed").toMap
  val built = dependencies.map(m => m -> s"$m has been built").toMap

  val afaAdapted = "AFA has been adapted"
  val afaBuilt = "AFA has been built"
  val afaDeployed = "AFA has been deployed"
  val yaafaDeveloped = "yaafa has been developed"
  val yaafaBuilt = "yaafa has been built"
  val yaafaDeployed = "yaafa has been deployed"

  goal(perfTest, uat)

  define { operator("Develope yaafa") assert yaafaDeveloped }
  define { operator("Build yaafa") require yaafaDeveloped assert yaafaBuilt }
  define { operator("Deploy yaafa") require yaafaBuilt assert yaafaDeployed }

  for (d <- dependencies) {
    define { operator(s"Built $d") assert built(d) }
    define { operator(s"Deploy $d") require built(d) assert deployed(d) }
  }

  define { operator("Adapt AFA") assert afaAdapted }
  define { operator("Built AFA") require afaAdapted assert afaBuilt }
  define { operator("Deploy AFA") require afaBuilt assert afaDeployed }

  define {
    operator("Run functional test") require deployed.values.toSet require afaDeployed require yaafaDeployed assert funcTest
  }

  define { operator("Run performance test") require funcTest assert perfTest }
  define { operator("Run user acceptance test") require funcTest assert uat }
}
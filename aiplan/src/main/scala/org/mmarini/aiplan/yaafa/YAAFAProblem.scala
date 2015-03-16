package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.PlanProblemDSL
import com.sun.org.apache.regexp.internal.REUtil
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException
import com.sun.org.apache.xpath.internal.operations.Mod

/**
 *
 */
object YAAAFAProblem extends PlanProblemDSL {
  val devEnv = "dev env"
  val intEnv = "integration env"

  val yaafa = "yaafa"
  val yaafaImplemented = s"$yaafa has been implemented"
  val yaafaDevConfigured = s"$yaafa has been configured in $devEnv"
  val yaafaDevTested = s"$yaafa has been tested in $devEnv"
  val yaafaIntDeployed = s"$yaafa has been tested in $intEnv"

  val afa = "AFA"
  val afaWrapper = "AFAWrapper"
  val afaMigrated = s"$afaWrapper has been migrated"
  val afaWrapperImplemented = s"$afaWrapper has been implemented"
  val afaConfigured = s"$afa has been configured in $devEnv"
  val afaDevTested = s"$afa has been tested in $devEnv"
  val afaIntDeployed = s"$afa has been deployed in $intEnv"
  val afaIntTested = s"$afa has been tested in $intEnv"

  //  val xfrDepApps = "IFG PRE XPI XAS XPS".split("""\s""").toSet

  val xfrMigrated = "XFrame has been migrated"

  val xfrDepAppMigrated = "XFrame dependencies have been migrated"
  val xfrDepAppConfigured = s"XFrame dependencies has been configured in $devEnv"
  val xfrDepAppIntDeployed = s"XFrame dependencies has been deployed in $intEnv"

  val devEnvConfigured = s"devEnv has been configured"
  val intDevConfigured = s"intEnv has been configured"

  /*
   * Configure dev environment
   */
  define { operator(s"Configure $devEnv") assert (devEnvConfigured) }

  /*
   * YYAFA
   */
  define { operator(s"Implement $yaafa") assert (yaafaImplemented) }
  define { operator(s"Configure $yaafa for $devEnv") require (yaafaImplemented) require (devEnvConfigured) assert (yaafaDevConfigured) }
  define { operator(s"Test $yaafa in $devEnv") require (yaafaDevConfigured) assert (yaafaDevTested) }

  /*
   * AFA
   */
  define { operator(s"Implement $afaWrapper") require (xfrMigrated) assert (afaWrapperImplemented) }
  define { operator(s"Configure $afa") require (devEnvConfigured) require (afaWrapperImplemented) assert (afaConfigured) }
  define {
    operator(s"Test $afa in $devEnv") require (afaConfigured) require (
      yaafaDevTested) require (xfrDepAppConfigured) assert (afaDevTested)
  }

  /*
   * Migrate XFrame
   */
  define { operator(s"Migrate XFrame") require (afaMigrated) require (xfrDepAppMigrated) assert (xfrMigrated) }
  define { operator(s"Migrate $afa") assert (afaMigrated) }
  define { operator(s"Migrate XFrame dependecies") assert (xfrDepAppMigrated) }

  /*
   * Configure dependencies in dev env
   */
  define { operator(s"Configure XFrame dependecies") require (xfrMigrated) require (devEnvConfigured) assert (xfrDepAppConfigured) }

  /*
   * Setup integration test
   */
  define { operator(s"Configure $intEnv") assert (intDevConfigured) }
  define { operator(s"Deploy $afa in $intEnv") require (intDevConfigured) require (afaDevTested) assert (afaIntDeployed) }
  define { operator(s"Deploy $yaafa in $intEnv") require (intDevConfigured) require (yaafaDevTested) assert (yaafaIntDeployed) }
  define { operator(s"Deploy XFrame dependencies in $intEnv") require (intDevConfigured) require (afaDevTested) assert (xfrDepAppIntDeployed) }
  define { operator("Run integration test") require (yaafaIntDeployed) require (afaIntDeployed) require (xfrDepAppIntDeployed) assert (afaIntTested) }

  goal(afaIntTested)
}
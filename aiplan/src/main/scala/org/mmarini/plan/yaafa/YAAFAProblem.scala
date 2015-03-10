package org.mmarini.aiplan.yaafa

import org.mmarini.aiplan.graphplan.Proposition
import org.mmarini.aiplan.graphplan.PlanProblem
import org.mmarini.aiplan.graphplan.StringProposition
import org.mmarini.aiplan.graphplan.Operator
import org.mmarini.aiplan.graphplan.State

/**
 *
 */
object YAAAFAProblem {

  trait Entity {
    def init: State = Set()
    def props: Set[Proposition]
    def ops: Set[Operator]
  }

  case class RuntimeEnv(id: String) extends Entity {

    def ready: Proposition = s"$id ready"

    def setUp = Operator(
      Set(),
      Set(ready),
      id = s"setUp $id")

    val props = Set(ready)

    def ops = props.map(Operator.apply) + setUp

  }

  val testEnv = RuntimeEnv("TestEnv")

  val runtimes = Set(testEnv)

  abstract class AppModule(val id: String) extends Entity {
    def built: Proposition = s"$id is built"

    def deployed(rte: RuntimeEnv): Proposition = s"$id is deployed in ${rte.id}"

    def build = Operator(
      Set(),
      Set(built),
      id = s"build $id")

    def deploy(in: RuntimeEnv) = Operator(
      Set(built, in.ready),
      Set(deployed(in)),
      id = s"deploy $id in ${in.id}")

    def props = runtimes.map(deployed) + built

    def ops = props.map(Operator.apply) + build ++ runtimes.map(deploy)
  }

  case class DependencyApp(override val id: String) extends AppModule(id) {
  }

  val dependencies = Set("IFG", "PRE", "UTL", "XPI", "XAS").map(DependencyApp)

  object Tests extends Entity {
    val performanceTestPassed: Proposition = s"Performance test has passed"

    val functionalTestPassed: Proposition = s"Functional test has passed"

    val uatTestPassed: Proposition = s"User acceptance test has passed"

    val allTestPassed: Proposition = "All tests have passed"

    val checkForTests = Operator(
      Set(performanceTestPassed, functionalTestPassed, uatTestPassed),
      Set(allTestPassed),
      id = "final check for tests")

    val runPerformanceTest = Operator(
      Set(functionalTestPassed),
      Set(performanceTestPassed),
      id = "run performance test")

    val runFunctionalTest = Operator(
      Set(),
      Set(functionalTestPassed),
      id = "run functional test")

    val runUATest = Operator(
      Set(functionalTestPassed),
      Set(uatTestPassed),
      id = "run user acceptance test")

    val props = Set(performanceTestPassed, functionalTestPassed, uatTestPassed, allTestPassed)

    val ops = props.map(Operator.apply) + checkForTests + runFunctionalTest + runPerformanceTest + runUATest
  }

  val goal = Set[Proposition](Tests.allTestPassed)

  val init = Set[Proposition]()

  val ops = Tests.ops

  val problem = PlanProblem(init, goal, ops)
}
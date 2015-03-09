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

  object AFA extends AppModule("AFA") {

    def offLoaded: Proposition = s"$id offloaded"

    def tested: Proposition = s"$id tested"

    override def build = Operator(
      Set(offLoaded),
      Set(built),
      id = s"build $id")

    def offLoad = Operator(
      Set(),
      Set(offLoaded),
      id = s"offLoad $id")

    def runTest = Operator(
      dependencies.map(_.deployed(testEnv)) + deployed(testEnv),
      Set(tested),
      id = s"test $id")

    override def props = super.props + offLoaded + tested

    override val ops = props.map(Operator.apply) ++ super.ops + build + offLoad + runTest
  }

  val goal = Set[Proposition](AFA.tested)

  val init = runtimes.map(_.init).flatten ++ dependencies.map(_.init).flatten ++ AFA.init

  val ops = runtimes.map(_.ops).flatten ++ dependencies.map(_.ops).flatten ++ AFA.ops

  val problem = PlanProblem(init, goal, ops)
}
package org.mmarini.aiplan.yaafa

import org.scalatest.FunSpec
import org.scalatest.Matchers
import org.mmarini.aiplan.graphplan.GraphPlanner
import YAAAFAProblem._

/**
 *
 */
class YAAFAPlannerTest extends FunSpec with Matchers {
  
  describe("A planner for YAAFAProblem") {
    val planner = new GraphPlanner(YAAAFAProblem.problem)

    describe("the plan") {
      val planOpt = planner.plan

      it("should exist and have size 2") {
        planOpt should not be empty
        println(planOpt.get.mkString("\n"))
      }
    }
  }

}
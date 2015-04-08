package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class GraphPlannerTest extends FunSpec with Matchers {

  def fullPlan(planner: GraphPlanner): StateLayer = {
    def expand(plan: StateLayer): StateLayer = {
      val ep = plan.next.next
      if (ep.isLastLayer)
        plan
      else
        expand(ep)
    }

    expand(planner.initialGraphPlan)
  }

  /**
   * Dobbiamo testare un caso dove all'uscita del primo ciclo di espansione
   * il grafo dei piani non contine il piano effettivo ma è necessario effettuare
   * almeno un'espansione ulteriore del piano.
   *
   * Goal CE
   * A -> BC -> BCE
   * A -> DE
   *
   * ?A+B-A
   * ?A+D-A
   * ?B+C
   * ?D+E
   * ?C+E
   *
   * Graph plan
   * S1: [A]
   * O1: [?A+A ?A+B-A, ?A+D-A]
   *       [(?A+A ?A+B-A) (?A+A ?A+D-A) (?A+B-A ?A+D-A)]
   * S2: [A B D] [AB AD BD]
   * O3: [?B+C ?D+E ?B+B ?D+D ?A+A ?A+B-A, ?A+D-A]
   *       [(?A+A ?A+B-A) (?A+A ?A+D-A) (?A+B-A ?A+D-A)
   *        (... any ops with oher ops)]
   * S3: [A B D C E] [ AB AD BD AC AE BE]
   */
  describe("Futher expansion test") {
    val problem = new PlanProblemDSL {
      init("A")
      goal("C", "E")
      define { operator("Add B") require ("A") assert ("B") deny ("A") }
      define { operator("Add D") require ("A") assert ("D") deny ("A") }
      define { operator("Add C") require ("B") assert ("C") }
      define { operator("Add E") require ("D") assert ("E") }
      define { operator("When C add E") require ("C") assert ("E") }
    }.apply
    val planner = new GraphPlanner(problem)

    describe("The plan") {
      val planOpt = planner.plan

      it("should have size 3") {
        planOpt should not be empty
        planOpt.get should have size 3

      }
      it("should coontain ...") {
        planOpt should not be empty
        val plan = planOpt.get.flatten.map(_.descr)
        plan should contain theSameElementsInOrderAs (List("Add A", "Add B", "Add A"))
      }
    }
  }

  /**
   *
   */
  describe("Given a problem with just one operator") {

    val problem = new PlanProblemDSL {
      init("init")
      define { operator("op1") require ("init") assert ("goal") }
    }.apply

    val planner = new GraphPlanner(problem)

    describe("the full expanded graph") {
      val fp = fullPlan(planner)

      it("should have 2 layers") {
        fp.depth shouldBe (2)
      }

      describe("the 2st back state layer") {
        val l = fp.parent.parent
        it("should contain init") {
          l.state should contain("init")
        }
        it("should contain 1 props") {
          l.state should have size 1
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the last state layer") {
        val l = fp
        it("should contain init,goal") {
          l.state should contain("init")
          l.state should contain("goal")
        }
        it("should contain 2 props") {
          l.state should have size 2
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the last op layer") {
        val l = fp.parent
        val descr = l.ops.map(_.descr).filterNot(_ startsWith ("nop("))
        it("should contain op1") {
          descr should contain("op1")
        }
        it("should contain 1 operator") {
          descr should have size 1
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
    }
  }

  describe("Given a problem with just one operator and mutex props") {

    val problem = new PlanProblemDSL {
      init("init")
      define { operator("op1") require ("init") assert ("prop1") deny ("init") }
      define { operator("op2") require ("init") assert ("prop2") deny ("init") }
    }.apply

    val planner = new GraphPlanner(problem)

    describe("the full planner") {
      val fp = fullPlan(planner)

      it("should have 2 depth") {
        fp.depth shouldBe (2)
      }

      describe("the 2nd back state layer") {
        val l = fp.parent.parent
        it("should contain init") {
          l.state should contain("init")
        }
        it("should contain 1 prop") {
          l.state should have size 1
        }
        it("should contain no mutex") {
          l.mutex shouldBe empty
        }
      }
      describe("the 2nd op layer") {
        val l = fp.parent
        val descr = l.ops.map(_.descr)
        it("should contain nop(init),op1,op2") {
          descr should contain("nop(init)")
          descr should contain("op1")
          descr should contain("op2")
        }
        it("should contain 3 operators") {
          descr should have size 3
        }
        describe("the mutex descr") {
          val md = l.mutex.map { case (a, b) => (a.descr, b.descr) }

          it("should contain (nop(init),prop1), ... mutex") {
            md should contain("nop(init)", "op1")
            md should contain("nop(init)", "op2")
            md should contain("op1", "op2")
          }
        }
        it("should contain 6 mutex") {
          l.mutex should have size 6
        }
      }
    }
  }
}
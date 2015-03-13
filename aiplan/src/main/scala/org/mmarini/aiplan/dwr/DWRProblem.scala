package org.mmarini.aiplan.dwr

import org.mmarini.aiplan.graphplan.PlanProblemDSL
import org.mmarini.aiplan.graphplan.PlanProblem
import org.mmarini.aiplan.graphplan.Operator

/**
 *
 */
object DWRProblem extends PlanProblemDSL {
  val robots = Set("R", "Q")
  val locations = Set("L1", "L2")
  val containers = Set("A", "B")

  def isAt(r: String, at: String) = s"$r is at $at"
  def isUnloaded(r: String) = s"$r is unloaded"
  def isOn(c: String, r: String) = s"$c is on $r"

  init(
    isAt("R", "L1"),
    isAt("Q", "L2"),
    isAt("A", "L1"),
    isAt("B", "L2"),
    isUnloaded("R"),
    isUnloaded("Q"))

  goal(isAt("A", "L2"), isAt("B", "L1"))

  for {
    r <- robots
    from <- locations
    to <- locations if (to != from)
  } define { operator(s"Move $r to $to") require isAt(r, from) assert isAt(r, to) deny isAt(r, from) }

  for {
    r <- robots
    c <- containers
    at <- locations
  } {
    define {
      operator(s"Load $c on $r").
        require(Set(isAt(r, at), isAt(c, at), isUnloaded(r))).
        assert(Set(isOn(c, r))).
        deny(Set(isAt(c, at), isUnloaded(r)))
    }
    define {
      operator(s"Unload $c from $r").
        require(Set(isAt(r, at), isOn(c, r))).
        assert(Set(isAt(c, at), isUnloaded(r))).
        deny(Set(isOn(c, r)))
    }
  }
}
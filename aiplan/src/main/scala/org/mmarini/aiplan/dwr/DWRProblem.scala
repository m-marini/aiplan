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

  init("R at L1", "Q at L2", "A at L1", "B at L2", "R unloaded", "Q unloaded")
  
  goal("A at L2", "B at L1")

  for {
    r <- robots
    from <- locations
    to <- locations if (to != from)
  } define { operator(s"Move $r to $to") require s"$r at $from" assert s"$r at $to" deny s"$r at $from" }

  for {
    r <- robots
    c <- containers
    at <- locations
  } {
    define {
      operator(s"Load $r with $c").
        require(Set(s"$r at $at", s"$c at $at", s"$r unloaded")).
        assert(Set(s"$c on $r")).
        deny(Set(s"$c at $at", s"$r unloaded"))
    }
    define {
      operator(s"Unload $r by $c").
        require(Set(s"$r at $at", s"$c on $r")).
        assert(Set(s"$c at $at", s"$r unloaded")).
        deny(Set(s"$c on $r"))
    }
  }
}
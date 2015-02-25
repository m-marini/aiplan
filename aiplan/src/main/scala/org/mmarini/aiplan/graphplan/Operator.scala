package org.mmarini.aiplan.graphplan

import com.sun.tracing.Probe

/**
 *
 */
case class Operator(preconditions: State, addEffects: State, delEffects: State, cost: Double = 1.0) {
  /**
   *
   */
  def apply(state: State): State = {
    require(isApplicable(state))
    state -- delEffects ++ addEffects
  }

  /**
   *
   */
  def isApplicable(state: State): Boolean =
    preconditions.subsetOf(state)

  /**
   *
   */
  def isIndipendent(op: Operator): Boolean =
    delEffects.intersect(op.addEffects.union(op.preconditions)).isEmpty &&
      op.delEffects.intersect(addEffects.union(preconditions)).isEmpty

  /**
   *
   */
  def isMutex(op: Operator, mutex: Set[(Proposition, Proposition)]): Boolean = !isIndipendent(op) ||
    !((for {
      a1 <- preconditions
      a2 <- op.preconditions
    } yield (a1, a2)).forall {
      case (a1, a2) => !(mutex.contains((a1, a2)) || mutex.contains((a2, a1)))
    })

  /**
   *
   */
  override def toString = s"(+{${addEffects.mkString(" ")}}, -{${delEffects.mkString(" ")}}, ?{${preconditions.mkString(" ")}})"
}
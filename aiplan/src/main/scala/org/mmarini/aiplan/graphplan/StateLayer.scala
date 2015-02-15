/**
 *
 */
package org.mmarini.aiplan.graphplan

/**
 * @author us00852
 *
 */
case class StateLayer(state: State, mutex: Set[(Proposition, Proposition)]) {

  /**
   *
   */
  def this(state: State) = this(state, Set())

  /**
   *
   */
  def next(ops: Set[Operator]): OpLayer = {
    val appOps = ops.filter(op => op.isApplicable(state) &&
      !combination(op.preconditions).exists(mutex.contains))
    val mutexOp = combination(appOps).filter { case (a, b) => a.isMutex(b, mutex) }
    OpLayer(appOps, mutexOp)
  }
}
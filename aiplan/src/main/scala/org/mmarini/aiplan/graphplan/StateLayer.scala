/**
 *
 */
package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging

/**
 * @author us00852
 *
 */
case class StateLayer(state: State, mutex: Set[(Proposition, Proposition)]) extends LazyLogging {

  /**
   *
   */
  def this(state: State) = this(state, Set())

  /**
   *
   */
  def next(ops: Set[Operator]): OpLayer = {
    val appOps0 = ops.filter(op => op.isApplicable(state))
    logger.debug("")
    val appOps = appOps0.filterNot(op => combination(op.preconditions).exists(c => {
      if (mutex.contains(c)) {
        logger.debug(s"Filtered $op")
        logger.debug(s" because $c")
        true
      } else
        false
    }))
    val mutexOp = combination(appOps).filter { case (a, b) => a.isMutex(b, mutex) }
    OpLayer(appOps, mutexOp)
  }
}
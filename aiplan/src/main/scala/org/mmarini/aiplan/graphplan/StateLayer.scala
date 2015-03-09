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
    // Filter all operator applicable to the state
    val appOps0 = ops.filter(op => op.isApplicable(state))
    // Filter out all operators that have mutex preconditions 
    val appOps = appOps0.filterNot(op => {
      val comb = combination(op.preconditions)
      val mux = mutex.exists(comb.contains)
      mux
    })

    /**
     * Two operators are mutex if:
     * - they are dependent or
     * - a precondition of op1 is mutex with a precondition of op2
     */
    def isMutex(ops: (Operator, Operator)): Boolean = ops match {
      case (a, b) =>
        {
          val mux1 = !a.isIndipendent(b)
          if (mux1) true
          else {
            val pa = a.preconditions
            val pb = b.preconditions
            val comb = combination(pa, pb)
            //            val mux2 = !comb.forall(!mutex.contains(_))
            val mux2 = mutex.exists(comb.contains)
            mux2
          }
        }
    }

    // Filter all operators that are mutes
    val mutexOp = combination(appOps).filter(isMutex)
    OpLayer(appOps, mutexOp)
  }

  /**
   *
   */
  def isSameLayer(other: StateLayer) =
    state == other.state && mutex == other.mutex

  /**
   * A state layer contains a state if
   * - goal is a subset of the layer states and
   * - any goal proposition is not mutex
   */
  def contains(goal: State) = {
    if (!goal.subsetOf(state)) false
    else {
      //      
      //      val inter = combination(other).intersect(mutex)
      //      if (!inter.isEmpty)
      //        logger.debug(s"mutex state $inter")
      //      !inter.isEmpty
      val comb = combination(goal)
      !mutex.exists(comb.contains)
    }
  }

  /**
   *
   */
  override def toString = state.mkString("\n")
}
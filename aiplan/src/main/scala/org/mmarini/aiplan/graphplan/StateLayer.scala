/**
 *
 */
package org.mmarini.aiplan.graphplan

import com.typesafe.scalalogging.LazyLogging
import scala.annotation.tailrec

/**
 * @author us00852
 *
 */
case class StateLayer(parent: OpLayer, state: State, mutex: Set[(String, String)], ops: Set[Operator]) extends LazyLogging {

  /**
   *
   */
  def this(p: PlanProblem) = this(null, p.init, Set(), p.ops)

  /**
   * Return the number of state layer to root
   */
  lazy val depth: Int = if (isRoot) 1 else parent.parent.depth + 1

  /**
   *
   */
  def expandUntilPattern(pattern: State): Option[StateLayer] = {
    logger.debug(s"[depth=$depth] Expanding ${state.size} states, ${mutex.size} mutex")
    if (contains(pattern)) {
      logger.debug(s"[depth=$depth] Pattern matched ${pattern.mkString(",")}")
      Some(this)
    } else if (isLastLayer) {
      logger.debug(s"[depth=$depth] No plan found for ${pattern.mkString(",")}")
      None
    } else {
      next.next.expandUntilPattern(pattern);
    }
  }

  /**
   *
   */
  def isRoot = parent == null

  /**
   *
   */
  def isLastLayer = !isRoot && isSameLayer(parent.parent)

  /**
   *
   */
  lazy val next: OpLayer = {
    // Filter all operator applicable to the state
    val appOps0 = ops.filter(op => op.isApplicable(state))
    // Filter out all operators that have mutex requirements 
    val appOps = appOps0.filterNot(op => {
      val comb = combination(op.requirements)
      val mux = mutex.exists(comb.contains)
      mux
    })

    /**
     * Two operators are mutex if:
     * - they are dependent or
     * - a requirement of op1 is mutex with a requirement of op2
     * 
     * or in other words:
     * - exists a denial of op1 that is a requirements or an assertion of op2 or
     * - exists a denial of op2 that is a requirements or an assertion of op1 or
     * - a requirement of op1 is mutex with a requirement of op2
     *
     * Two operators are NOT mutex if:
     * - they are independent and
     * - a requirement of op1 is not mutex with a requirement of op2
     *
     */
    def isMutex(ops: (Operator, Operator)): Boolean = ops match {
      case (a, b) =>
        {
          val mux1 = !a.isIndipendent(b)
          if (mux1) true
          else {
            val pa = a.requirements
            val pb = b.requirements
            val comb = combination(pa, pb)
            //            val mux2 = !comb.forall(!mutex.contains(_))
            val mux2 = mutex.exists(comb.contains)
            mux2
          }
        }
    }

    // Filter all operators that are mutes
    val mutexOp = combination(appOps).filter(isMutex)
    OpLayer(this, appOps, mutexOp)
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
   * It searches backward the graph for a plan.
   * It returns the plan or if not found the noGoodTables created while traversing the graph
   */
  def backwardSearch(
    goal: State,
    blackList: StateSetList): (Option[Plan], StateSetList) = {

    // Check if initial state layer has reached (no action layer)
    if (isRoot) {
      (Some(Nil), blackList)
    } else if (blackList.head.contains(goal))
      // check if the no goodTable contains the goal
      (None, blackList)
    else {
      gpSearch(goal, Set(), blackList) match {
        // check if no plan has found and add the goal to noGoodTable
        case (None, newBlackList) => {
          logger.debug(s"Add goal ${goal.mkString(",")} to black list")
          val nextBlackList = (newBlackList.head + goal) :: newBlackList.tail
          (None, nextBlackList)
        }
        // check for found plan
        case ret => ret
      }
    }
  }

  /**
   * Given a graph plan and the current goal, it searches backward the graph for a plan.
   * It returns the plan or if not found the noGoodTables created while traversing the graph.
   * If the has some propositions it creates an operation set to reduce the proposition of goal
   * When the goal is reduced to empty set it searches for the previous layer
   */
  def gpSearch(
    goal: State,
    plan: PartialPlan,
    blackList: StateSetList): (Option[Plan], StateSetList) = {

    // Check for empty goal (search backward previuos layer)
    if (goal.isEmpty) {
      // create the goal for previuos layer
      val nextGoal = plan.map(_.requirements).flatten
      // search backward previuos layer
      parent.parent.backwardSearch(nextGoal, blackList.tail) match {
        // check for no plan found
        case (None, nextBlackList) => {
          logger.debug(s"Black list on exit\n  ${nextBlackList.mkString("  \n")}")
          (None, blackList.head :: nextBlackList)
        }
        // check for plan found
        case (Some(nextPlan), nextBlackList) => (Some(plan :: nextPlan), blackList.head :: nextBlackList)
      }
    } else {
      val opLayer = parent

      // Select an operator set that satisfies the first goal propositions
      val ops1 = opLayer.providers(goal.head)

      // Filter out the operator mutex with any other operators in the plan
      val ops = if (plan.isEmpty) ops1
      else ops1.filterNot(op => {
        val oo = plan.map((op, _))
        oo.exists(opLayer.mutex)
      })
      val sorted = ops.toList.sortWith(_.cost <= _.cost)

      // check if no operators are available
      // Iterate for an action available
      @tailrec
      def loopForAction(ops: List[Operator], blackList: StateSetList): (Option[Plan], StateSetList) =
        if (ops.isEmpty) {
          logger.debug(s"Plan not found for ${goal.mkString(",")}")
          return (None, blackList)
        } else {
          // Select the operator with lower cost
          val op = ops.head
          // create the new goal removing all precondition from the goal
          val ng = goal -- op.assertions
          // create the new  partial plan 
          val np = plan + op
          // repeat search with the new parms
          gpSearch(ng, np, blackList) match {
            case (Some(plan), newBlackList) => {
              logger.debug(s"Plan found for ${ops.mkString(",")}")
              (Some(plan), newBlackList)
            }
            case (None, newBlackList) => loopForAction(ops.tail, newBlackList)
          }
        }

      loopForAction(sorted, blackList)
    }
  }

  /**
   *
   */
  override def toString = state.mkString("\n")
}
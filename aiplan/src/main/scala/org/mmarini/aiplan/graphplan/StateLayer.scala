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
  def expandUntilPattern(pattern: State): Option[StateLayer] =
    if (contains(pattern)) {
      logger.debug(s"Pattern matched ${pattern.mkString(",")} at $depth")
      Some(this)
    } else if (isLastLayer) {
      logger.debug(s"No plan found for ${pattern.mkString(",")}")
      None
    } else
      next.next.expandUntilPattern(pattern);

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
  def next: OpLayer = {
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
     * - a precondition of op1 is mutex with a precondition of op2
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
    unmatches: StateSetList): (Option[Plan], StateSetList) = {

    // Check if initial state layer has reached (no action layer)
    if (isRoot) {
      (Some(Nil), unmatches)
    } else if (unmatches.head.contains(goal))
      // check if the no goodTable contains the goal
      (None, unmatches)
    else {
      gpSearch(goal, Set(), unmatches) match {
        // check if no plan has found and add the goal to noGoodTable
        case (None, newUnmatches) => (None, (newUnmatches.head + goal) :: newUnmatches.tail)
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
    unmatches: StateSetList): (Option[Plan], StateSetList) = {

    // Check for empty goal (search backward previuos layer)
    if (goal.isEmpty) {
      // create the goal for previuos layer
      val nextGoal = plan.map(_.requirements).flatten
      // search backward previuos layer
      parent.parent.backwardSearch(nextGoal, unmatches.tail) match {
        // check for no plan found
        case (None, nextUnmatches) => (None, unmatches.head :: nextUnmatches)
        // check for plan found
        case (Some(nextPlan), nextUnmatches) => (Some(plan :: nextPlan), unmatches.head :: nextUnmatches)
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
      def loopForAction(ops: List[Operator], unmatches: StateSetList): (Option[Plan], StateSetList) =
        if (ops.isEmpty) {
          logger.debug(s"Plan not found for ${goal.mkString(",")}")
          return (None, unmatches)
        } else {
          // Select the operator with lower cost
          val op = ops.head
          // create the new goal removing all precondition from the goal
          val ng = goal -- op.assertions
          // create the new  partial plan 
          val np = plan + op
          // repeat search with the new parms
          gpSearch(ng, np, unmatches) match {
            case (Some(plan), newUnmatches) => {
              logger.debug(s"Plan found for ${ops.mkString(",")}")
              (Some(plan), newUnmatches)
            }
            case (None, newUnmatches) => loopForAction(ops.tail, newUnmatches)
          }
        }

      loopForAction(sorted, unmatches)
    }
  }

  /**
   *
   */
  override def toString = state.mkString("\n")
}
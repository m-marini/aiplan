package org.mmarini.aiplan.graphplan

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Op

/**
 *
 */
case class OpLayer(ops: Set[Operator], mutex: Set[(Operator, Operator)]) {

  /**
   *
   */
  def next: StateLayer = StateLayer(nextProps, nextMutex)

  /**
   *
   */
  def nextMutex = {
    val props = nextProps
    val map = mapPropOp

    /**
     * Two propositions are mutex if:
     * - every operator in the layer that has p1 as positive effect is mutex
     *   with every operator in the layer that has p2 as positive effect, and
     * - there is no single operator in the layer that has both p1 and p2 as
     *   positive effects
     */
    def isPropMutex(p1: Proposition, p2: Proposition) = {
      val both = Set(p1, p2)
      val mux1 = !ops.exists(op => both.subsetOf(op.addEffects))
      if (mux1) {
        val op1 = mapPropOp(p1)
        val op2 = mapPropOp(p2)
        val comb = combination(op1, op2)
        val mux2 = comb.forall(mutex.contains)
        mux2
      } else
        false
    }

    combination(props).filter {
      case (a, b) => isPropMutex(a, b)
    }
  }
  /**
   *
   */
  def nextProps = ops.map(_.addEffects).flatten

  /**
   * Create a map between propositions and the operators that has the proposition as positive effects
   */
  def mapPropOp = {
    // Get the set of positive effects and generator operator
    val s1 = for (op <- ops.toSeq) yield op.addEffects.map((_, op))
    val s2 = s1.flatten

    // Group by proposition
    val s3 = s2.groupBy {
      case (p, _) => p
    }
    val s4 = s3.map {
      case (prop, opList) => (prop, opList.map { case (_, op) => op }.toSet)
    }
    s4
  }

  /**
   *
   */
  def providers(p: Proposition): Set[Operator] = ops.filter(_.addEffects.contains(p))

  /**
   *
   */
  override def toString = ops.mkString("\n")

}
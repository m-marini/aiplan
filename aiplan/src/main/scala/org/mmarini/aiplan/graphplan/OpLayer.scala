package org.mmarini.aiplan.graphplan

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Op

/**
 *
 */
case class OpLayer(parent: StateLayer, ops: Set[Operator], mutex: Set[(Operator, Operator)]) {

  /**
   *
   */
  lazy val next: StateLayer = StateLayer(this, nextProps, nextMutex, parent.ops)

  /**
   *
   */
  def nextMutex = {
    val props = nextProps
    val map = mapPropOp

    /**
     * Two propositions are mutex if:
     * - there is no single operator in the layer that asserts both p1 and p2 and
     * - every operator in the layer that asserts p1 is mutex
     *   with every operator in the layer that asserts p2
     *   
     * Two propositions are NOT mutex if:
     * - there is at least one operator in the layer that has asserts p1 and p2  or
     * - every operator in the layer that asserts p1 is not mutex
     *   with every operator in the layer that asserts p2
     */
    def isPropMutex(p1: String, p2: String) = {
      val both = Set(p1, p2)
      val mux1 = !ops.exists(op => both.subsetOf(op.assertions))
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
  def nextProps = ops.map(_.assertions).flatten

  /**
   * Create a map between propositions and the operators that has the proposition as positive effects
   */
  def mapPropOp = {
    // Get the set of positive effects and generator operator
    val s1 = for (op <- ops.toSeq) yield op.assertions.map((_, op))
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
  def providers(p: String): Set[Operator] = ops.filter(_.assertions.contains(p))

  /**
   *
   */
  override def toString = ops.mkString("\n")

}
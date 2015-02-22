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
    def isPropMutex(p1: Proposition, p2: Proposition) =
      combination(mapPropOp(p1), mapPropOp(p2)).forall(mutex.contains)
    combination(props).filter {
      case (a, b) => isPropMutex(a, b)
    }
  }
  /**
   *
   */
  def nextProps = ops.map(_.addEffects).flatten

  /**
   *
   */
  def mapPropOp =
    ops.map(op => op.addEffects.map((_, op))).flatten.groupBy {
      case (p, _) => p
    }.map {
      case (p, l) => (p -> l.map {
        case (p, op) => op
      })
    }

  /**
   *
   */
  def providers(p: Proposition): Set[Operator] = ops.filter(_.addEffects.contains(p))

  /**
   *
   */
  override def toString = s"{${ops.mkString(" ")}}"

}
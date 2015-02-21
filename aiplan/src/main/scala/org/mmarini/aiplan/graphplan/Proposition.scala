/**
 *
 */
package org.mmarini.aiplan.graphplan

/**
 * @author us00852
 *
 */
trait Proposition {
}

case class StringProposition(id: String) extends Proposition {

  /**
   *
   */
  override def toString = id
}
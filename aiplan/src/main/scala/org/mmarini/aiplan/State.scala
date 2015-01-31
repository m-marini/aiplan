package org.mmarini.aiplan

/**
 *
 */
trait State[O <: Operator] {
  /**
   *
   */
  def operators: Set[O]

  /**
   * 
   */
  def apply(o: O): State[O]
}

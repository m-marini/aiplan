package org.mmarini.aiplan

/**
 *
 */
trait State[O] {
  /**
   *
   */
  def operators: Set[O]
}

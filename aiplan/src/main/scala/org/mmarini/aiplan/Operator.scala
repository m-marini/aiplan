package org.mmarini.aiplan

/**
 *
 */
trait Operator[S] {

  /**
   *
   */
  def apply(s: S): S

  /**
   *
   */
  def cost: Double
}
package org.mmarini.sfs

trait Predicate {
  def not: Predicate
}

object False extends Predicate {
  def not = True
}

object True extends Predicate {
  def not = False
}
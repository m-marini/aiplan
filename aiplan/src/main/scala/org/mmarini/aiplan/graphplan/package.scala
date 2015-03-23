package org.mmarini.aiplan

package object graphplan {

  /**
   *
   */
  def combination[A](x: Set[A], y: Set[A]): Set[(A, A)] =
    for {
      a <- x
      b <- y if (a != b)
    } yield (a, b)

  /**
   *
   */
  def combination[A](x: Set[A]): Set[(A, A)] = combination(x, x)

  type State = Set[String]
    
  type PartialPlan = Set[Operator]
  type Plan = List[PartialPlan]
  type OrderedPlan = List[Operator]
  type StateSet = Set[State]
  type StateSetList = List[StateSet]

}
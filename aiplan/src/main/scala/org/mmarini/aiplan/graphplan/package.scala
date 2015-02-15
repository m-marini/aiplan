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

  /**
   *
   */
  implicit def StringToProps(id: String) = Proposition(id)
}
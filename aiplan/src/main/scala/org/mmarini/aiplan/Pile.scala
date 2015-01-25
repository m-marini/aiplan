package org.mmarini.aiplan

/**
 *
 */
case class Pile(location: Location.Value, containers: List[Container.Value]) {

  /**
   *
   */
  def push(c: Crane) = Pile(location, c.payload.get :: containers)

  /**
   *
   */
  def pop = Pile(location, containers.tail)

  /**
   *
   */
  lazy val isEmpty = containers.isEmpty
}
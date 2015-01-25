package org.mmarini.aiplan

/**
 *
 */
case class Crane(location: Location.Value, payload: Option[Container.Value]) {
  /**
   *
   */
  def take(p: Pile) = Crane(location, Some(p.containers.head))

  /**
   *
   */
  def take(r: Robot) = Crane(location, r.payload)

  /**
   *
   */
  def drop = Crane(location, None)

  /**
   *
   */
  lazy val isEmpty = payload.isEmpty
}
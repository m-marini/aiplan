package org.mmarini.aiplan

/**
 *
 */
case class Robot(id: Int, location: Location.Value, payload: Option[Container.Value]) {

  /**
   *
   */
  def move(l: Location.Value) = Robot(id, l, payload)

  /**
   *
   */
  def load(c: Crane) = Robot(id, location, c.payload)

  /**
   *
   */
  def unload = Robot(id, location, None)

  /**
   *
   */
  lazy val isEmpty = payload.isEmpty
}
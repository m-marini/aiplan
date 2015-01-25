package org.mmarini.aiplan

/**
 *
 */
trait Action extends Operator[DWRState] {

  /**
   *
   */
  def precondition(state: DWRState): Boolean

  /**
   *
   */
  val cost = 1.0
}

/**
 *
 */
case class Move(robot: Robot, to: Location.Value) extends Action {

  /**
   *
   */
  def precondition(state: DWRState) = Location.isAdjacent(robot.location, to) && state.isEmpty(to)

  /**
   *
   */
  def apply(state: DWRState) = state.update(robot.move(to))
}

/**
 *
 */
case class Load(robot: Robot, crane: Crane) extends Action {

  /**
   *
   */
  def precondition(state: DWRState): Boolean = robot.location == crane.location && robot.isEmpty && !crane.isEmpty

  /**
   *
   */
  def apply(state: DWRState): DWRState = state.update(robot.load(crane)).update(crane.drop)
}

/**
 *
 */
case class Unload(robot: Robot, crane: Crane) extends Action {

  /**
   *
   */
  def precondition(state: DWRState) = robot.location == crane.location && !robot.isEmpty && crane.isEmpty

  /**
   *
   */
  def apply(state: DWRState) = state.update(robot.unload).update(crane.take(robot))
}

/**
 *
 */
case class Take(crane: Crane, pile: Pile) extends Action {

  /**
   *
   */
  def precondition(state: DWRState) = crane.location == pile.location && crane.isEmpty && !pile.isEmpty

  /**
   *
   */
  def apply(state: DWRState) = state.update(crane.take(pile)).update(pile.pop)
}

/**
 *
 */
case class Drop(crane: Crane, pile: Pile) extends Action {

  /**
   *
   */
  def precondition(state: DWRState) = crane.location == pile.location && !crane.isEmpty

  /**
   *
   */
  def apply(state: DWRState) = state.update(crane.drop).update(pile.push(crane))
}
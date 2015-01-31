package org.mmarini.aiplan

/**
 *  The state is a set of values described by functions
 *  In our case the state is a set of objects each of them has properties
 *  so the state values are the properties of objects
 */
case class DWRState(cranes: Seq[Crane], piles: Seq[Pile], robots: Seq[Robot]) extends State[Action] {

  /**
   *
   */
  def apply(a: Action) = a(this)

  /**
   *
   */
  def isEmpty(location: Location.Value): Boolean = robots.forall(_.location != location)

  /**
   *
   */
  def update(c: Crane) = DWRState(c +: cranes.filterNot(_.location == c.location), piles, robots)

  /**
   *
   */
  def update(p: Pile) = DWRState(cranes, p +: piles.filterNot(_.location == p.location), robots)

  /**
   *
   */
  def update(r: Robot) = DWRState(cranes, piles, r +: robots.filterNot(_.id == r.id))

  /**
   *
   */
  def operators =
    (
      (for {
        r <- robots
        to <- Location.values
      } yield Move(r, to)).filter(_.precondition(this)) ++
      (for {
        r <- robots
        c <- cranes
      } yield Load(r, c)).filter(_.precondition(this)) ++
      (for {
        r <- robots
        c <- cranes
      } yield Unload(r, c)).filter(_.precondition(this)) ++
      (for {
        c <- cranes
        p <- piles
      } yield Take(c, p)).filter(_.precondition(this)) ++
      (for {
        c <- cranes
        p <- piles
      } yield Drop(c, p)).toSet.filter(_.precondition(this))).toSet
}

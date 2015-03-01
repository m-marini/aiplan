package org.mmarini.aiplan.graphplan

import javax.sql.rowset.Predicate

/**
 *
 */
object DWRProblem {

  case class Robot(id: String) {

    def at(l: Location): Proposition = s"$id.at(${l.id})"

    val unloaded: Proposition = s"$id.unloaded"

    def move(from: Location, to: Location) = Operator(
      Set(at(from)),
      Set(at(to)),
      Set(at(from)))

    def load(cont: Container, loc: Location) = Operator(
      Set(at(loc), cont.at(loc), unloaded),
      Set(cont.on(this)),
      Set(cont.at(loc), unloaded))

    def unload(cont: Container, loc: Location) = Operator(
      Set(at(loc), cont.on(this)),
      Set(cont.at(loc), unloaded),
      Set(cont.on(this)))
  }
  case class Location(id: String) {}

  case class Container(id: String) {
    def on(r: Robot): Proposition = s"$id.on(${r.id})"
    def at(l: Location): Proposition = s"$id.at(${l.id})"
  }

  object R extends Robot("R")
  object Q extends Robot("Q")

  object L1 extends Location("1")
  object L2 extends Location("2")

  object A extends Container("A")
  object B extends Container("B")

  val robots = Set(R, Q)
  val locations = Set(L1, L2)
  val containers = Set(A, B)

  val states = (for {
    r <- robots
    l <- locations
  } yield r.at(l)) ++
    (for {
      c <- containers
      l <- locations
    } yield c.at(l)) ++
    (for {
      c <- containers
      r <- robots
    } yield c.on(r)) ++
    (for { r <- robots }
      yield r.unloaded)

  val ops = (for {
    r <- robots
    l1 <- locations
    l2 <- locations if (l1 != l2)
  } yield r.move(l1, l2)) ++
    (for {
      c <- containers
      r <- robots
      l <- locations
    } yield r.load(c, l)) ++
    (for {
      c <- containers
      r <- robots
      l <- locations
    } yield r.unload(c, l)) ++
    states.map(Operator(_))

  val problem = PlanProblem(
    Set(R.at(L1),
      Q.at(L2),
      A.at(L1),
      B.at(L2),
      R.unloaded,
      Q.unloaded),
    Set(
      A.at(L2),
      B.at(L1)),
    ops)
}
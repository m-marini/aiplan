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

  object robotR extends Robot("R")
  object robotQ extends Robot("Q")

  object location1 extends Location("1")
  object location2 extends Location("2")

  object containerA extends Container("A")
  object containerB extends Container("B")

  val robots = Set(robotR, robotQ)
  val locations = Set(location1, location2)
  val containers = Set(containerA, containerB)

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
    states.map(p => Operator(Set(p), Set(p), Set()))

  val problem = PlanProblem(
    State(Set(robotR.at(location1),
      robotQ.at(location2),
      containerA.at(location1),
      containerB.at(location2),
      robotR.unloaded,
      robotQ.unloaded)),
    State(Set(
      containerA.at(location2),
      containerB.at(location1))),
    ops)
}
package org.mmarini.aiplan

object Main extends App {
  import Location._
  import Container._

  def goal(s: DWRState) =
    s.piles.contains(Pile(Location2, List(Container1, Container2)))

  /**
   *
   */
  val initial = DWRState(
    Seq(Crane(Location1, None), Crane(Location2, None), Crane(Location3, None)),
    Seq(Pile(Location1, List(Container1, Container2)), Pile(Location2, List()), Pile(Location3, List())),
    Seq(Robot(1, Location2, None)))

  val plan = Planner.plan(initial, goal, (State) => 0.0)

  println(plan.get.mkString("\n"))
}
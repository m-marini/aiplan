package org.mmarini.aiplan

object MainDWR extends App {
  import Location._
  import Container._

  def goal(s: State[Action]) =
    s.asInstanceOf[DWRState].piles.contains(Pile(Location2, List(Container2, Container1, Container4, Container3)))

  /**
   * 78393 / 105283
   */
  val initial = DWRState(
    Seq(Crane(Location1, None), Crane(Location2, None), Crane(Location3, None)),
    Seq(Pile(Location1, List(Container1, Container2, Container3, Container4)), Pile(Location2, List()), Pile(Location3, List())),
    Seq(Robot(1, Location2, None)))

  def heuristic(s: State[Action]): Double = {
    val p = s.asInstanceOf[DWRState].piles.find(_.location == Location2).get
    4 - p.containers.size
  }

  val plan = Planner.plan[Action](initial, goal, heuristic)

  if (plan.isEmpty)
    println("No plan")
  else
    println(plan.get.mkString("\n"))
}
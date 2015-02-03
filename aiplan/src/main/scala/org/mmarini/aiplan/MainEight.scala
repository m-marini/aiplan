package org.mmarini.aiplan
import scala.math.abs
import com.typesafe.scalalogging.LazyLogging

object MainEight extends App with LazyLogging {

  val goalSeq = IndexedSeq(None, Some(1), Some(2), Some(3), Some(4), Some(5), Some(6), Some(7), Some(8))
  val initState = IndexedSeq(Some(7), Some(2), Some(4), Some(5), None, Some(6), Some(8), Some(3), Some(1))

  def goal(s: State[EightMove]) = s.asInstanceOf[EightGame].pos == goalSeq

  def distance(a: Int, b: Int) = {
    val xa = a % 3
    val ya = a / 3
    val xb = b % 3
    val yb = b / 3
    abs(xa - xb) + abs(ya - yb)
  }

  def heuristic(s: State[EightMove]): Double =
    (for { i <- 0 to 8 } yield {
      val ns = s.asInstanceOf[EightGame].pos(i)
      val j = goalSeq.indexOf(ns)
      distance(i, j)
    }).sum

  val plan = Planner.plan[EightMove](EightGame(initState), goal, heuristic)

  if (plan.isEmpty)
    logger.info("No plan")
  else
    logger.info(plan.get.mkString("\n"))
}
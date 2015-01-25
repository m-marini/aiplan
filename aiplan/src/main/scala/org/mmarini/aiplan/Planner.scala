package org.mmarini.aiplan

/**
 *
 */
object Planner {

  /**
   * State
   *
   */
  def plan(si: DWRState, goal: (DWRState) => Boolean, heuristic: (DWRState) => Double): Option[Seq[Action]] = {

    case class SearchNode(plan: Seq[Action]) {
      def this() = this(Seq())

      /**
       *
       */
      lazy val state: DWRState = if (plan.isEmpty)
        si
      else {
        def toPath(ctx: (Seq[Action], DWRState)): (Seq[Action], DWRState) = ctx match {
          case (Nil, _) => ctx
          case (p, s) => toPath(p.tail, p.head(s))
        }
        toPath(plan.reverse, si)._2
      }

      /**
       *
       */
      lazy val cost: Double = g + heuristic(state)

      /**
       *
       */
      def g = plan.map(_.cost).sum
    }

    def best(fringe: Set[SearchNode], visited: Set[DWRState]): Option[Seq[Action]] = {
      def select: SearchNode = fringe.reduce((a, b) => if (a.cost <= b.cost) a else b)

      if (fringe.isEmpty)
        None
      else {
        val sn = select
        if (goal(sn.state))
          Some(sn.plan)
        else {
          // New visited
          val nv = visited + sn.state
          // New fringe
          val ss = (for { o <- sn.state.operators }
            yield SearchNode(o +: sn.plan)).filterNot(sn => nv.contains(sn.state))
          val nf = (fringe - sn) ++ ss
          best(nf, nv)
        }
      }
    }

    best(Set(new SearchNode), Set()) match {
      case Some(s) => Some(s.reverse)
      case _ => None
    }

  }
}
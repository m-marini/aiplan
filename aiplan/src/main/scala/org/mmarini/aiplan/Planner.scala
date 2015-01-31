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

    case class SearchNode(action: Option[Action], tail: Option[SearchNode]) {
      require(action.isEmpty && tail.isEmpty || !action.isEmpty && !tail.isEmpty)

      /**
       *
       */
      lazy val state: DWRState =
        if (action.isEmpty) si else
          action.get.apply(tail.get.state)

      /**
       *
       */
      lazy val cost: Double = g + heuristic(state)

      /**
       *
       */
      lazy val g: Double = if (action.isEmpty) 0 else
        action.get.cost + tail.get.g

      def actionPath: Seq[Action] = {
        def loop(path: Seq[Action], n: SearchNode): Seq[Action] =
          if (n.action.isEmpty)
            path
          else
            loop(n.action.get +: path, n.tail.get)
        loop(Nil, this)
      }
    }

    def best(n: Int, fringe: Map[DWRState, SearchNode], visited: Set[DWRState]): (Int, Option[SearchNode]) = {

      def select: (DWRState, SearchNode) = fringe.reduce((a, b) => if (a._2.cost <= b._2.cost) a else b)
      if (fringe.isEmpty)
        (n, None)
      else {
        val sn = select._2
        println(s"n=$n fringe=${fringe.size} g=${sn.g} cost=${sn.cost}")

        if (goal(sn.state))
          (n, Some(sn))
        else {
          // New visited
          val nv = visited + sn.state
          // New fringe
          val ss = (for { o <- sn.state.operators.toSeq }
            yield SearchNode(Some(o), Some(sn))).filterNot(sn => nv.contains(sn.state))

          def merge(sn: Map[DWRState, SearchNode], nn: Seq[SearchNode]): Map[DWRState, SearchNode] =
            if (nn.isEmpty)
              sn
            else {
              val duplicateNode = sn.get(nn.head.state)
              val nsn =
                if (duplicateNode.isEmpty)
                  sn + (nn.head.state -> nn.head)
                else if (nn.head.g < duplicateNode.get.g)
                  (sn - (duplicateNode.get.state)) + (nn.head.state -> nn.head)
                else
                  sn
              merge(nsn, nn.tail)
            }

          val nf = merge(fringe - sn.state, ss)
          best(n + 1, nf, nv)
        }
      }
    }

    best(0, Map(si -> SearchNode(None, None)), Set()) match {
      case (_, Some(node)) => Some(node.actionPath)
      case _ => None
    }

  }
}
package org.mmarini.aiplan

import com.typesafe.scalalogging.LazyLogging

/**
 *
 */
object Planner extends LazyLogging {

  /**
   * State
   *
   */
  def plan[O <: Operator](si: State[O], goal: (State[O]) => Boolean, heuristic: (State[O]) => Double): Option[Seq[O]] = {

    case class SearchNode(op: Option[O], tail: Option[SearchNode]) {
      require(op.isEmpty && tail.isEmpty || !op.isEmpty && !tail.isEmpty)

      /**
       *
       */
      lazy val state: State[O] =
        if (op.isEmpty) si else
          tail.get.state(op.get)

      /**
       *
       */
      lazy val cost: Double = g + heuristic(state)

      /**
       *
       */
      lazy val g: Double = if (op.isEmpty) 0 else op.get.cost + tail.get.g

      def actionPath: Seq[O] = {
        def loop(path: Seq[O], n: SearchNode): Seq[O] =
          if (n.op.isEmpty)
            path
          else
            loop(n.op.get +: path, n.tail.get)
        loop(Nil, this)
      }
    }

    def best(n: Int, fringe: Map[State[O], SearchNode], visited: Set[State[O]]): (Int, Option[SearchNode]) = {

      def select: (State[O], SearchNode) = fringe.reduce((a, b) => if (a._2.cost <= b._2.cost) a else b)
      if (fringe.isEmpty)
        (n, None)
      else {
        val sn = select._2
        logger.info(s"n=$n fringe=${fringe.size} g=${sn.g} cost=${sn.cost}")

        if (goal(sn.state))
          (n, Some(sn))
        else {
          // New visited
          val nv = visited + sn.state
          // New fringe
          val ss = (for { o <- sn.state.operators.toSeq }
            yield SearchNode(Some(o), Some(sn))).filterNot(sn => nv.contains(sn.state))

          def merge(sn: Map[State[O], SearchNode], nn: Seq[SearchNode]): Map[State[O], SearchNode] =
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
package org.mmarini.aiplan.tiles

import org.mmarini.aiplan.graphplan.Operator
import org.mmarini.aiplan.graphplan.State
import org.mmarini.aiplan.graphplan.PlanProblemDSL

/**
 *
 */
abstract class TileGame(n: Int, m: Int) extends PlanProblemDSL {
  require(n > 0)
  require(m > 0)

  def at(r: Int, c: Int) = s"($r,$c)"

  def tile(i: Int) = s"[$i]"

  val locations = for {
    row <- 0 until n
    col <- 0 until m
  } yield at(row, col)

  val tiles = (for (id <- 1 to (m * n - 1)) yield tile(id)).toSet

  /**
   *
   */
  val topology = (for {
    r <- 0 until n
    c <- 0 until m
  } yield {
    val n1 = if (r > 0) Set(at(r - 1, c)) else Set[String]()
    val n2 = if (c > 0) n1 + at(r, c - 1) else n1
    val n3 = if (r < n - 1) n2 + at(r + 1, c) else n2
    val n4 = if (c < m - 1) n3 + at(r, c + 1) else n3
    (at(r, c) -> n4)
  }).toMap

  for {
    holeLocation <- locations
    tileLocation <- topology(holeLocation)
    tile <- tiles
  } {
    define {
      operator(s"Move $tile to $holeLocation").
        require(Set(holeAt(holeLocation), tileAt(tile, tileLocation))).
        assert(Set(holeAt(tileLocation), tileAt(tile, holeLocation))).
        deny(Set(holeAt(holeLocation), tileAt(tile, tileLocation)))
    }
  }

  /**
   *
   */
  def tileAt(id: String, at: String) = s"$id at $at"

  /**
   *
   */
  def holeAt(at: String) = s"[ ] at $at"

  /**
   *
   */
  def state(s: String) =
    s.split("\\s").zip(locations).map {
      case ("-", loc) => holeAt(loc)
      case (name, loc) => tileAt(tile(name.toInt), loc)
    }.toSet

  goal(holeAt(at(0, 0)))

  goal(locations.drop(1).zip(tiles).map {
    case (loc, tile) => tileAt(tile, loc)
  }.toSet)
}

object ThreeProblem extends TileGame(2, 2) {
  init(state("1 3 - 2"))
}

object EightProblem extends TileGame(3, 3) {
  init(state("7 2 4 5 - 6 8 3 1"))
}
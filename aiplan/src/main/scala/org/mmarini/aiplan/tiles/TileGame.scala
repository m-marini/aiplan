package org.mmarini.aiplan.tiles

import org.mmarini.aiplan.graphplan.Operator
import org.mmarini.aiplan.graphplan.State

/**
 *
 */
abstract class TileGame(n: Int, m: Int) {
  require(n > 0)
  require(m > 0)

  /**
   *
   */
  case class Location(row: Int, col: Int) {
    require(row >= 0)
    require(row < n)
    require(col >= 0)
    require(col < m)

    def this(l: (Int, Int)) = this(l._1, l._2)

    override def toString = s"$row,$col"
  }

  /**
   *
   */
  case class Tile(id: String) {
    def at(l: Location) = s"$id.at($l)"

    def at(r: Int, c: Int): String = at(Location(r, c))
  }

  /**
   *
   */
  object hole {
    def at(l: Location) = s"hole.at(${l})"

    def at(r: Int, c: Int): String = at(Location(r, c))

    def at(l: (Int, Int)): String = at(new Location(l))

    def move(from: Location, to: Location, tile: Tile) =
      Operator(
        Set(at(from), tile.at(to)),
        Set(at(to), tile.at(from)),
        Set(at(from), tile.at(to)))

    def move(from: (Int, Int), to: (Int, Int), tile: Int): Operator =
      move(new Location(from), new Location(to), tiles(tile))

  }

  /**
   *
   */
  val locations = for {
    r <- 0 until n
    c <- 0 until m
  } yield Location(r, c)

  /**
   *
   */
  val tiles = for (i <- 1 to (n * m) - 1) yield Tile(i.toString)

  /**
   *
   */
  def state(s: String): State = s.split("\\s").zip(locations).map {
    case ("-", loc) => hole.at(loc)
    case (name, loc) => Tile(name).at(loc)
  }.toSet

  /**
   *
   */
  val topology: Map[Location, Set[Location]] = (for {
    r <- 0 until n
    c <- 0 until m
  } yield {
    val l = Location(r, c)
    val n1 = if (r > 0) List(Location(r - 1, c)) else Nil
    val n2 = if (c > 0) Location(r, c - 1) :: n1 else n1
    val n3 = if (r < n - 1) Location(r + 1, c) :: n2 else n2
    val n4 = if (c < m - 1) Location(r, c + 1) :: n3 else n3
    (l -> n4.toSet)
  }).toMap

  /**
   *
   */
  val holeProps = (for (l <- locations) yield hole.at(l)).toSet

  /**
   *
   */
  val tileProps = (for {
    l <- locations
    t <- tiles
  } yield t.at(l)).toSet

  /**
   *
   */
  val props = holeProps ++ tileProps

  /**
   *
   */
  val holeOps = (for {
    lh <- locations
    tile <- tiles
    lt <- topology(lh)
  } yield hole.move(lh, lt, tile))

  /**
   *
   */
  val nops = props.map(Operator(_))

  /**
   *
   */
  val ops = nops ++ holeOps

}
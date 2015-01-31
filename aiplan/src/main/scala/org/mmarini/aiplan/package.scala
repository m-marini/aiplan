/**
 *
 */
package org.mmarini

/**
 * @author us00852
 *
 */
package object aiplan {
  object Container extends Enumeration {
    val Container1 = Value("C1")
    val Container2 = Value("C2")
    val Container3 = Value("C3")
    val Container4 = Value("C4")
  }

  object Location extends Enumeration {
    val Location1 = Value("L1")
    val Location2 = Value("L2")
    val Location3 = Value("L3")

    private val topology = Set((Location1, Location2), (Location1, Location3), (Location2, Location3))

    /**
     *
     */
    def isAdjacent(l1: Value, l2: Value) = topology.contains((l1, l2)) || topology.contains((l2, l1))

  }
}
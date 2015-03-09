package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class OperatorTest extends FunSpec with Matchers {
  describe("An operator") {
    val a = Operator(Set[Proposition]("a"), Set[Proposition]("b"), Set[Proposition]("c"))
    it("should not be mutex with itself") {
      a.isIndipendent(a) should be(true)
    }
  }

  describe("An operator with precondition contained in other del effect") {
    val a = Operator(Set[Proposition]("a"), Set[Proposition]())
    val b = Operator(Set[Proposition](), Set[Proposition](), Set[Proposition]("a"))
    it("should be mutex") {
      a.isIndipendent(b) should be(false)
      b.isIndipendent(a) should be(false)
    }
  }

  describe("An operator with add effect contained in other del effect") {
    val a = Operator(Set[Proposition](), Set[Proposition]("a"))
    val b = Operator(Set[Proposition](), Set[Proposition](), Set[Proposition]("a"))
    it("should be mutex with itself") {
      a.isIndipendent(b) should be(false)
      b.isIndipendent(a) should be(false)
    }
  }
}
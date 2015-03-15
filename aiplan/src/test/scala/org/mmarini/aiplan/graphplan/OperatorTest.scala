package org.mmarini.aiplan.graphplan

import org.scalatest.FunSpec
import org.scalatest.Matchers

/**
 *
 */
class OperatorTest extends FunSpec with Matchers {
  describe("An operator") {
    val a = Operator(Set("a"), Set("b"), Set("c"))
    it("should not be mutex with itself") {
      a.isIndipendent(a) should be(true)
    }
  }

  describe("An operator with precondition contained in other del effect") {
    val a = Operator(Set("a"), Set())
    val b = Operator(Set[String](), Set[String](), Set("a"))

    it("should be mutex") {
      a.isIndipendent(b) should be(false)
      b.isIndipendent(a) should be(false)
    }
  }

  describe("An operator with add effect contained in other del effect") {
    val a = Operator(Set[String](), Set("a"))
    val b = Operator(Set[String](), Set[String](), Set("a"))
    it("should be mutex with itself") {
      a.isIndipendent(b) should be(false)
      b.isIndipendent(a) should be(false)
    }
  }
}
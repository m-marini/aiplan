object Test {
  Set(1) ++ Set(2)                                //> res0: scala.collection.immutable.Set[Int] = Set(1, 2)
  Set(1) ++: Set(2)                               //> res1: scala.collection.immutable.Set[Int] = Set(1, 2)
  
  Seq(1) ++ Seq(2)                                //> res2: Seq[Int] = List(1, 2)
  Seq(1) ++: Seq(2)                               //> res3: Seq[Int] = List(1, 2)
}
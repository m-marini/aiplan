import a.TestRec

object Test {
	new TestRec                               //> java.lang.Error: bho
                                                  //| 	at a.TestRec.rec(TestRec.scala:7)
                                                  //| 	at a.TestRec.<init>(TestRec.scala:9)
                                                  //| 	at Test$$anonfun$main$1.apply$mcV$sp(Test.scala:4)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$$anonfun$$exe
                                                  //| cute$1.apply$mcV$sp(WorksheetSupport.scala:76)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.redirected(W
                                                  //| orksheetSupport.scala:65)
                                                  //| 	at org.scalaide.worksheet.runtime.library.WorksheetSupport$.$execute(Wor
                                                  //| ksheetSupport.scala:75)
                                                  //| 	at Test$.main(Test.scala:3)
                                                  //| 	at Test.main(Test.scala)
}
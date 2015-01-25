lazy val commonSettings = Seq(
  organization := "org.mmarini",
  version := "0.1.0",
  scalaVersion := "2.11.4"
)

lazy val scalaToolVersion = "2.11"

lazy val root = (project in file(".")).
  settings(commonSettings: _*).
  settings(
    name := "aiplan",
    libraryDependencies ++= Seq(
    	"com.github.scala-incubator.io" % s"scala-io-file_$scalaToolVersion" % "0.4.3-1",
    	"org.scalatest" % s"scalatest_$scalaToolVersion" % "2.2.2" % Test,
		"org.scalamock" % s"scalamock-core_$scalaToolVersion" % "3.1.2" % Test,
		"org.scalacheck" % s"scalacheck_$scalaToolVersion" % "1.11.6" % Test
	)
  )

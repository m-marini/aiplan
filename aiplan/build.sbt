organization := "org.mmarini"

version := "0.1.0"

scalaVersion := "2.11.5"

name := "aiplan"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.3-1"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % Test

libraryDependencies += "org.scalamock" %% "scalamock-core" % "3.1.2" % Test

libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.11.6" % Test

lazy val root = project in file(".")

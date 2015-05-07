name := "we-lab3-group45"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

libraryDependencies ++= Seq(
  javaJdbc,
  javaCore,
  javaJpa,
  cache,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.1.Final",
  "com.google.code.gson" % "gson" % "2.2"
)

enablePlugins(SbtTwirl)

TwirlKeys.templateImports += "scala.collection._"

TwirlKeys.templateImports += "at.ac.tuwien.big.we15.lab2.api._"

fork in run := true


fork in run := true
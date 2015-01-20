name := """SYMPLProject"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
  "mysql" % "mysql-connector-java" % "5.1.18",
  "com.googlecode.json-simple" % "json-simple" % "1.1.1",
  "com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.0",
  "com.eaio.uuid" % "uuid" % "3.2"
)
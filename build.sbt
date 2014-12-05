name := """callouts"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  javaJdbc,
  javaEbean,
  cache,
  javaWs,
	"mysql" % "mysql-connector-java" % "5.1.18",
	"com.typesafe.play.plugins" %% "play-plugins-mailer" % "2.3.1",
	"com.google.code.gson" % "gson" % "2.3.1"
)

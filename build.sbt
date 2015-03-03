name := "github-license-service"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.5"

libraryDependencies ++= Seq(
  ws,
  "org.apache.commons" % "commons-lang3" % "3.3.2",
  "org.scalatestplus" %% "play" % "1.2.0" % "test"
)
lazy val root = (project in file(".")).enablePlugins(PlayScala)

name := "github-license-service"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  ws,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % "test"
)

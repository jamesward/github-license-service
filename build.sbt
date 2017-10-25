lazy val root = (project in file(".")).enablePlugins(PlayScala)

name := "github-license-service"

scalaVersion := "2.12.4"

libraryDependencies ++= Seq(
  ws,
  guice,
  "org.apache.commons" % "commons-text" % "1.1",
  "org.scalatestplus.play" % "scalatestplus-play_2.12" % "3.1.2" % "test"
)

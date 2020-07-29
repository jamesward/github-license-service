enablePlugins(PlayScala)

name := "github-license-service"

scalaVersion := "2.13.3"

libraryDependencies ++= Seq(
  ws,
  guice,
  "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % "test"
)

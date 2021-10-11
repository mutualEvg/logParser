name := "json-logs"

version := "0.1"

scalaVersion := "2.13.6"

idePackagePrefix := Some("com.evg")

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe"  %% "circe-core"     % circeVersion,
  "io.circe"  %% "circe-generic"  % circeVersion,
  "io.circe"  %% "circe-parser"   % circeVersion,
  "io.circe"  %% "circe-optics"   % circeVersion,
  "org.scalatest" %% "scalatest" % "3.0.8" % Test
)
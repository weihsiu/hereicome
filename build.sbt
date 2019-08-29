val dottyVersion = "0.17.0-RC1"

name := "dotty-simple"
version := "0.1.0"
scalaVersion := dottyVersion

scalacOptions ++= Seq(
  "-language:implicitConversions"
)
libraryDependencies ++= Seq(
  ("org.typelevel" %% "cats-core" % "2.0.0-RC2").withDottyCompat(scalaVersion.value)
)

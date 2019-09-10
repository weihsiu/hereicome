val dottyVersion = "0.18.1-RC1"

name := "dotty-simple"
version := "0.1.0"
scalaVersion := dottyVersion

scalacOptions ++= Seq(
  "-language:implicitConversions"
)

libraryDependencies ++= Seq(
  "com.eed3si9n.verify" %% "verify" % "0.2.0" % Test,
  ("org.typelevel" %% "cats-core" % "2.0.0").withDottyCompat(scalaVersion.value),
  ("org.typelevel" %% "cats-effect" % "2.0.0").withDottyCompat(scalaVersion.value)
)

testFrameworks += new TestFramework("verify.runner.Framework")

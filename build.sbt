val Http4sVersion = "0.20.0-M6"
val LogbackVersion = "1.2.3"
val CirceVersion = "0.11.1"
val CirceConfigVersion = "0.6.1"
val CatsVersion = "1.6.0"
val H2Version = "1.4.198"
val ScalaTestVersion = "3.0.6"
val ScalaCheckVersion = "1.14.0"

lazy val root = (project in file("."))
  .settings(
    organization := "fr.xebia",
    name := "xke-http4s",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.8",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-core" % CatsVersion,
      "io.circe" %% "circe-generic" % CirceVersion,
      "io.circe" %% "circe-parser" % CirceVersion,
      "io.circe" %% "circe-config" % CirceConfigVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s" %% "http4s-circe" % Http4sVersion,
      "org.http4s" %% "http4s-dsl" % Http4sVersion,
      "com.h2database" % "h2" % H2Version,
      "org.http4s" %% "http4s-blaze-client" % Http4sVersion % Test,
      "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
      "org.scalacheck" %% "scalacheck" % ScalaCheckVersion % Test,
      "ch.qos.logback" % "logback-classic" % LogbackVersion
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4")
  )

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-language:higherKinds",
  "-language:postfixOps",
  "-feature",
  "-Ypartial-unification",
  "-Xfatal-warnings",
)

val Http4sVersion = "0.20.0-M6"
val LogbackVersion = "1.2.3"
val CirceVersion = "0.11.1"
val CirceConfigVersion = "0.6.1"
val CatsVersion = "1.6.0"
val H2Version = "1.4.198"
val ScalaTestVersion = "3.0.6"
val ScalaCheckVersion = "1.14.0"

lazy val sharedDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % LogbackVersion,
    "io.circe" %% "circe-generic" % CirceVersion,
    "io.circe" %% "circe-parser" % CirceVersion,
    "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
    "org.scalacheck" %% "scalacheck" % ScalaCheckVersion % Test,
    "org.typelevel" %% "cats-core" % CatsVersion,
    "org.http4s" %% "http4s-blaze-client" % Http4sVersion % Test,
  )

lazy val infraDependencies = Seq(
    "org.http4s" %% "http4s-circe" % Http4sVersion,
    "org.http4s" %% "http4s-dsl" % Http4sVersion,
  )

lazy val sharedSettings = Seq(
    libraryDependencies ++= sharedDependencies,
    organization := "fr.xebia",
    scalaVersion := "2.12.8",
    version := "0.0.1-SNAPSHOT",
  )

lazy val domain = project
    .settings(
      name := "domain",
      sharedSettings,
    )

lazy val infrastructure = project
    .dependsOn(domain)
    .settings(
      libraryDependencies ++= infraDependencies,
      libraryDependencies ++= Seq(
        "com.h2database" % "h2" % H2Version,
      ),
      name := "infrastructure",
      sharedSettings,
    )

lazy val root = (project in file("."))
  .aggregate(domain, infrastructure)
  .dependsOn(domain, infrastructure)
  .settings(
    name := "xke-http4s",
    libraryDependencies ++= Seq(
      "io.circe" %% "circe-config" % CirceConfigVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.6"),
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.2.4"),
    sharedSettings,
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

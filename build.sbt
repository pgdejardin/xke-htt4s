val Http4sVersion = "0.20.0-M6"
val LogbackVersion = "1.2.3"
val CirceVersion = "0.11.1"
val CirceConfigVersion = "0.6.1"
val CatsVersion = "1.6.0"
val DoobieVersion = "0.6.0"
val ScalaTestVersion = "3.0.6"
val ScalaCheckVersion = "1.14.0"
val FlywayVersion = "5.2.4"
val PostgresVersion = "42.2.5"

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
  "org.flywaydb" % "flyway-core" % FlywayVersion,
  "org.http4s" %% "http4s-circe" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "org.postgresql" %% "postgresql" % PostgresVersion,
  "org.tpolecat" %% "doobie-core" % DoobieVersion,
  "org.tpolecat" %% "doobie-hikari" % DoobieVersion,
  "org.tpolecat" %% "doobie-postgres" % DoobieVersion,
  "org.tpolecat" %% "doobie-scalatest" % DoobieVersion,
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

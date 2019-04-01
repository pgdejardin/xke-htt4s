val Http4sVersion      = "0.20.0-M6"
val LogbackVersion     = "1.2.3"
val CirceVersion       = "0.11.1"
val CirceConfigVersion = "0.6.1"
val CatsVersion        = "1.6.0"
val DoobieVersion      = "0.6.0"
val ScalaTestVersion   = "3.0.6"
val ScalaCheckVersion  = "1.14.0"
val FlywayVersion      = "5.2.4"
val FuuidVersion       = "0.2.0-M7"

lazy val sharedDependencies = Seq(
  "ch.qos.logback"    % "logback-classic"      % LogbackVersion,
  "io.circe"          %% "circe-generic"       % CirceVersion,
  "io.circe"          %% "circe-parser"        % CirceVersion,
  "org.scalatest"     %% "scalatest"           % ScalaTestVersion % Test,
  "org.scalacheck"    %% "scalacheck"          % ScalaCheckVersion % Test,
  "org.typelevel"     %% "cats-core"           % CatsVersion,
  "org.http4s"        %% "http4s-blaze-client" % Http4sVersion % Test,
  "io.chrisdavenport" %% "fuuid"               % FuuidVersion,
  "io.chrisdavenport" %% "fuuid-circe"         % FuuidVersion,
)

lazy val infraDependencies = Seq(
  "org.flywaydb"      % "flyway-core"       % FlywayVersion,
  "org.http4s"        %% "http4s-circe"     % Http4sVersion,
  "org.http4s"        %% "http4s-dsl"       % Http4sVersion,
  "org.tpolecat"      %% "doobie-core"      % DoobieVersion,
  "org.tpolecat"      %% "doobie-hikari"    % DoobieVersion,
  "org.tpolecat"      %% "doobie-postgres"  % DoobieVersion,
  "org.tpolecat"      %% "doobie-scalatest" % DoobieVersion,
  "io.chrisdavenport" %% "fuuid-http4s"     % FuuidVersion,
  "io.chrisdavenport" %% "fuuid-doobie"     % FuuidVersion,
)

lazy val sharedSettings = Seq(
  libraryDependencies ++= sharedDependencies,
  organization := "fr.xebia",
  scalaVersion := "2.12.8",
  version := "0.0.1-SNAPSHOT",
)

lazy val `domain-core` = project
  .dependsOn(`domain-boundaries`)
  .settings(
    name := "domain-core",
    sharedSettings,
  )

lazy val `domain-boundaries` = project
  .settings(
    name := "domain-boundaries",
    sharedSettings,
  )

lazy val infrastructure = project
  .dependsOn(`domain-boundaries`)
  .settings(
    libraryDependencies ++= infraDependencies,
    name := "infrastructure",
    sharedSettings,
  )

lazy val root = (project in file("."))
  .aggregate(`domain-core`, `domain-boundaries`, infrastructure)
  .dependsOn(`domain-core`, `domain-boundaries`, infrastructure)
  .settings(
    name := "xke-http4s",
    libraryDependencies ++= Seq(
      "io.circe"   %% "circe-config"        % CirceConfigVersion,
      "org.http4s" %% "http4s-blaze-server" % Http4sVersion,
    ),
    addCompilerPlugin("org.spire-math" %% "kind-projector"     % "0.9.6"),
    addCompilerPlugin("com.olegpy"     %% "better-monadic-for" % "0.2.4"),
    sharedSettings,
  )

scalacOptions ++= Seq(
   // format: off
  "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
  "-encoding", "utf-8",                // Specify character encoding used by source files.
  "-explaintypes",                     // Explain type errors in more detail.
  "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
  "-language:existentials",            // Existential types (besides wildcard types) can be written and inferred
  "-language:experimental.macros",     // Allow macro definition (besides implementation and application)
  "-language:higherKinds",             // Allow higher-kinded types
  "-language:implicitConversions",     // Allow definition of implicit functions called views
  "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
  "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
  "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
  "-Xfuture",                          // Turn on future language features.
  "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
  "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
  "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
  "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
  "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
  "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
  "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
  "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
  "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
  "-Xlint:option-implicit",            // Option.apply used implicit view.
  "-Xlint:package-object-classes",     // Class or object defined in package object.
  "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
  "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
  "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
  "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
  "-Xlint:unsound-match",              // Pattern match may not be typesafe.
  "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
  "-Ypartial-unification",             // Enable partial unification in type constructor inference
  "-Ywarn-dead-code",                  // Warn when dead code is identified.
  "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
  "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
  "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
  "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
  "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
  "-Ywarn-numeric-widen",              // Warn when numerics are widened.
  "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
  "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
  "-Ywarn-unused:locals",              // Warn if a local definition is unused.
  "-Ywarn-unused:params",              // Warn if a value parameter is unused.
  "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
  "-Ywarn-unused:privates",            // Warn if a private member is unused.
  "-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
  // format: on
)

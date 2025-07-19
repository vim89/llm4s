// build.sbt — at project root

// =========== Project metadata & versions ===========
ThisBuild / organization := "org.llm4s.template"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "2.13.16"
val llm4sVersion = "0.1.1"
val scalaMuniteTestVersion = "1.1.1"

// Enable SemanticDB for Scalafix semantic rules
ThisBuild / semanticdbEnabled  := true
ThisBuild / semanticdbVersion  := scalafixSemanticdb.revision

// =========== Dependencies ===========
libraryDependencies ++= Seq(
  "org.llm4s" %% "llm4s" % llm4sVersion,
  "org.scalameta" %% "munit" % scalaMuniteTestVersion % Test,

  // Logger dependencies
  "ch.qos.logback" % "logback-classic" % "1.2.10", // Logback backend
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5" // scala-logging wrapper
  // integrating scala-logging into LLM (Large Language Model) scala applications is a recommended practice,
  // especially when using SLF4J and Logback.
  // This combination is widely adopted in the Scala ecosystem for its simplicity, performance, and compatibility with structured logging


  // Other Logger dependencies: For FP + LLM pipelines - LogStage is a top-tier choice—structured, efficient, observable
  // "io.7mind.izumi" %% "logstage-core" % "1.2.19", // core
  // "io.7mind.izumi" %% "logstage-rendering-circe" % "1.2.19", // JSON output
  // "io.7mind.izumi" %% "logstage-adapter-slf4j" % "1.2.19"    // optional SLF4J integration
  // LogStage, Woof are community‑endorsed for modern stacks but, not officially approved by scala core.
)

// Scalafix dependencies (needed for custom rules or built-ins)
ThisBuild / scalafixDependencies += "ch.epfl.scala" %% "scalafix-rules" % "0.14.3" // adjust version as needed

// =========== Compiler options ===========
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",         // warn about use of deprecated APIs
  "-feature",             // warn about misused features
  "-unchecked",           // additional warnings for unhandled cases
  "-Xlint",               // recommended additional warnings
  "-Ywarn-dead-code",     // warn when dead code is identified
  "-Ywarn-unused",        // warn when local defs unused
  "-encoding", "UTF-8",    // specify character encoding
  {
    if (scalaVersion.value.startsWith("2.12"))
      "-Ywarn-unused-import"
    else
      "-Wunused:imports"
  }
)

// =========== Project definition ===========
lazy val root = (project in file("."))
  .settings(
    name := "llm4s-template",
    Compile / mainClass := Some(organization.value), // optional
    Compile / scalafmtOnCompile := false // turn off automatic formatting on compile
  )
  .enablePlugins() // Add plugins as needed

// =========== Best Practices ===========
compileOrder := CompileOrder.Mixed
Global / onChangedBuildSource := ReloadOnSourceChanges

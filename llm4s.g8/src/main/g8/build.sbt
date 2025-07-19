// build.sbt — at project root

// =========== Project metadata & versions ===========
ThisBuild / organization := "$package$"
ThisBuild / version := "$version$"
ThisBuild / scalaVersion := "$scala_version$"
val llm4sVersion = "$llm4s_version$"
val scalaMuniteTestVersion = "$munit_version$"

// Enable SemanticDB for Scalafix semantic rules
ThisBuild / semanticdbEnabled  := true
ThisBuild / semanticdbVersion  := scalafixSemanticdb.revision

// =========== Dependencies ===========
libraryDependencies ++= Seq(
  "org.llm4s" %% "llm4s" % llm4sVersion,
  "org.scalameta" %% "munit" % scalaMuniteTestVersion % Test
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
    name := "$name$",
    Compile / mainClass := Some(organization.value), // optional
    Compile / scalafmtOnCompile := false // turn off automatic formatting on compile
  )
  .enablePlugins() // Add plugins as needed

// =========== Best Practices ===========
compileOrder := CompileOrder.Mixed
Global / onChangedBuildSource := ReloadOnSourceChanges

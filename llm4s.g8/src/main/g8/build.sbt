ThisBuild / organization := "$organization$"
ThisBuild / version := "$version$"
ThisBuild / scalaVersion := "$scala_version$"
ThisBuild / llm4sVersion = "$llm4s_version$"

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    publish / skip := true,
    libraryDependencies += "org.llm4s" %% "llm4s" % llm4sVersion
  )
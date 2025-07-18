ThisBuild / organization := "$package$"
ThisBuild / version := "$version$"
ThisBuild / scalaVersion := "$scala_version$"
ThisBuild / llm4sVersion := "$llm4s_version$"
ThisBuild / munitVersion := "$munit_version$"

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    publish / skip := true,
    libraryDependencies += Seq(
      "org.llm4s" %% "llm4s" % llm4sVersion,
      "org.scalameta" %% "munit" % munitVersion % Test
    )
  )


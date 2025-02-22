import Dependencies._

ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.llm4s"
ThisBuild / organizationName := "llm4s"

lazy val root = (project in file("."))
  .dependsOn(shared)
  .settings(
    name := "llm4s",
    libraryDependencies ++= List(
      "com.azure" % "azure-ai-openai" % "1.0.0-beta.14",
      munit       % Test
    )
  )

lazy val shared = (project in file("shared"))
  .settings(
    name := "shared",
    libraryDependencies ++= List(
      "com.lihaoyi" %% "upickle" % "4.1.0"
    )
  )

lazy val workspaceRunner = (project in file("workspaceRunner"))
  .dependsOn(shared)
  .settings(
    name := "workspace-runner",
    libraryDependencies ++= List(
      "com.lihaoyi" %% "upickle" % "4.1.0"
    )
  )

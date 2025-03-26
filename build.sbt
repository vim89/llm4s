ThisBuild / scalaVersion     := "2.13.16"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "org.llm4s"
ThisBuild / organizationName := "llm4s"

// Scalafmt configuration
ThisBuild / scalafmtOnCompile := true

lazy val root = (project in file("."))
  .aggregate(shared, workspaceRunner)
  .dependsOn(shared)
  .dependsOn(shared)
  .settings(
    name := "llm4s",
    libraryDependencies ++= List(
      "com.azure"      % "azure-ai-openai" % "1.0.0-beta.15",
      "com.anthropic"  % "anthropic-java"  % "0.8.0",
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "com.knuddels" % "jtokkit" % "1.1.0",
      "com.lihaoyi"   %% "upickle"         % "4.1.0",
      "com.lihaoyi"   %% "requests"        % "0.9.0",
      "org.scalatest" %% "scalatest"       % "3.2.19" % Test
    )
  )

lazy val shared = (project in file("shared"))
  .settings(
    name := "shared",
    libraryDependencies ++= List(
      "com.lihaoyi"   %% "upickle"         % "4.1.0",
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "org.scalatest" %% "scalatest"       % "3.2.19" % Test
    )
  )

lazy val workspaceRunner = (project in file("workspaceRunner"))
  .dependsOn(shared)
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(DockerPlugin)
  .settings(
    Docker / maintainer := "llm4s",
    dockerExposedPorts  := Seq(8080),
    dockerBaseImage     := "eclipse-temurin:21-jdk",
//    Compile / mainClass := Some("com.llm4s.runner.RunnerMain"),
    name := "workspace-runner",
    libraryDependencies ++= List(
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "com.lihaoyi"   %% "upickle"         % "4.1.0",
      "com.lihaoyi"   %% "cask"            % "0.10.2",
      "org.scalatest" %% "scalatest"       % "3.2.19" % Test
    )
  )

lazy val samples = (project in file("samples"))
  .dependsOn(shared, root)
  .settings(
    name := "samples",
    libraryDependencies ++= List(
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "org.scalatest" %% "scalatest"       % "3.2.19" % Test
    )
  )

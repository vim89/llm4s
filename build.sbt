import sbt.Keys._
import scoverage.ScoverageKeys._
import Common._

inThisBuild(
  List(
    crossScalaVersions := List(scala213, scala3),
    scalaVersion       := scala3,
    organization       := "org.llm4s",
    organizationName   := "llm4s",
    versionScheme      := Some("early-semver"),
    homepage := Some(url("https://github.com/llm4s/")),
    licenses := List("MIT" -> url("https://mit-license.org/")),
    developers := List(
      Developer(
        "rorygraves",
        "Rory Graves",
        "rory.graves@fieldmark.co.uk",
        url("https://github.com/rorygraves")
      )
    ),
    ThisBuild / publishTo := {
      val centralSnapshots = "https://central.sonatype.com/repository/maven-snapshots/"
      if (isSnapshot.value) Some("central-snapshots".at(centralSnapshots))
      else localStaging.value
    },
    pgpPublicRing := file("/tmp/public.asc"),
    pgpSecretRing := file("/tmp/secret.asc"),
    pgpPassphrase := sys.env.get("PGP_PASSPHRASE").map(_.toArray),
    scmInfo := Some(
      ScmInfo(
        url("https://github.com/llm4s/llm4s/"),
        "scm:git:git@github.com:llm4s/llm4s.git"
      )
    ),
    version := {
      dynverGitDescribeOutput.value match {
        case Some(out) if !out.isSnapshot() =>
          out.ref.value.stripPrefix("v")
        case Some(out) =>
          val baseVersion = out.ref.value.stripPrefix("v")
          s"$baseVersion+${out.commitSuffix.mkString("", "", "")}-SNAPSHOT"
        case None =>
          "0.0.0-UNKNOWN"
      }
    },
    ThisBuild / coverageMinimumStmtTotal := 80,
    ThisBuild / coverageFailOnMinimum    := false,
    ThisBuild / coverageHighlighting     := true,
    ThisBuild / coverageExcludedPackages :=
      """
        |org\.llm4s\.runner\..*
        |org\.llm4s\.samples\..*
      """.stripMargin.replaceAll("\n", ";"),
    ThisBuild / (coverageReport / aggregate) := false,
    // --- scalafix ---
    ThisBuild / scalafixDependencies += "ch.epfl.scala" %% "scalafix-rules" % "0.12.1",
    ThisBuild / scalafixOnCompile := true
  )
)

// ---- Handy aliases ----
addCommandAlias("cov", ";clean;coverage;test;coverageAggregate;coverageReport;coverageOff")
addCommandAlias("covReport", ";clean;coverage;test;coverageReport;coverageOff")
addCommandAlias("buildAll", ";clean;+compile;+test")
addCommandAlias("publishAll", ";clean;+publish")
addCommandAlias(
  "testAll",
  ";test;+publishLocal;++2.13.16 crossTestScala2/test;++3.7.1 crossTestScala3/test"
)
addCommandAlias(
  "cleanTestAll",
  ";clean;testAll"
)
addCommandAlias(
  "cleanTestAllAndFormat",
  ";scalafmtAll;cleanTestAll"
)
addCommandAlias("compileAll", ";+compile")
addCommandAlias("testCross", ";++2.13.16 crossTestScala2/test;++3.7.1 crossTestScala3/test")
addCommandAlias("fullCrossTest", ";clean ;crossTestScala2/clean ;crossTestScala3/clean ;+publishLocal ;testCross")



// ---- shared settings ----
lazy val commonSettings = Seq(
  Compile / scalacOptions := scalacOptionsForVersion(scalaVersion.value),
  Test / scalacOptions    := scalacOptionsForVersion(scalaVersion.value),
  semanticdbEnabled       := CrossVersion.partialVersion(scalaVersion.value).exists(_._1 == 3),
  Test / scalafix / unmanagedSources := Seq.empty,
  Compile / packageDoc / publishArtifact := !isSnapshot.value,
  libraryDependencies ++= Seq(
    Deps.cats,
    Deps.upickle,
    Deps.logback,
    Deps.monocleCore,
    Deps.monocleMacro,
    Deps.scalatest % Test,
    Deps.scalamock % Test,
    Deps.fansi,
    Deps.postgres,
    Deps.config,
    Deps.hikariCP
  )
)

// ---- projects ----
lazy val llm4s = (project in file("."))
  .aggregate(core, samples, workspaceShared, workspaceRunner, workspaceClient, workspaceSamples)

lazy val core = (project in file("modules/core"))
  .settings(
    name := "core",
    commonSettings,
    Test / fork := true,
    Compile / mainClass := None,
    Compile / discoveredMainClasses := Seq.empty,
    resolvers += "Vosk Repository" at "https://alphacephei.com/maven/",
    libraryDependencies ++= Seq(
      Deps.azureOpenAI,
      Deps.anthropic,
      Deps.jtokkit,
      Deps.requests,
      Deps.websocket,
      Deps.scalatest % Test,
      Deps.scalamock % Test,
      Deps.sttp,
      Deps.ujson,
      Deps.pdfbox,
      Deps.commonsIO,
      Deps.tika,
      Deps.poi,
      Deps.requests,
      Deps.jsoup,
      Deps.jna,
      Deps.vosk,
      Deps.postgres,
      Deps.config,
      Deps.hikariCP
    )
  )

lazy val workspaceShared = (project in file("modules/workspace/workspaceShared"))
  .settings(
    name := "workspaceShared",
    commonSettings,
    Compile / discoveredMainClasses := Seq.empty
  )

lazy val workspaceClient = (project in file("modules/workspace/workspaceClient"))
  .dependsOn(workspaceShared, core)
  .settings(
    name := "workspaceShared",
    commonSettings,
    Compile / discoveredMainClasses := Seq.empty,
    libraryDependencies ++= Seq(
      Deps.azureOpenAI,
      Deps.anthropic,
      Deps.jtokkit,
      Deps.requests,
      Deps.websocket,
      Deps.scalatest % Test,
      Deps.scalamock % Test,
      Deps.sttp,
      Deps.ujson,
      Deps.pdfbox,
      Deps.commonsIO,
      Deps.tika,
      Deps.poi,
      Deps.requests,
      Deps.jsoup,
      Deps.jna,
      Deps.vosk,
      Deps.postgres,
      Deps.config,
      Deps.hikariCP
    )
  )

lazy val workspaceRunner = (project in file("modules/workspace/workspaceRunner"))
  .dependsOn(workspaceShared)
  .enablePlugins(JavaAppPackaging, DockerPlugin)
  .settings(
    name := "workspaceRunner",
    commonSettings,
    Compile / mainClass := Some("org.llm4s.runner.RunnerMain"),
    libraryDependencies ++= Seq(
      Deps.cask,
      Deps.requests,
      Deps.postgres,
      Deps.config,
      Deps.hikariCP
    ),
    publish / skip := true
  )
  .settings(WorkspaceRunnerDocker.settings)

lazy val samples = (project in file("modules//samples"))
  .dependsOn(core)
  .settings(
    name := "samples",
    commonSettings,
    publish / skip := true
  )

lazy val workspaceSamples = (project in file("modules/workspace/workspaceSamples"))
  .dependsOn(workspaceShared, workspaceRunner, workspaceClient, samples)
  .settings(
    name := "workspaceSamples",
    commonSettings,
    publish / skip := true
  )

lazy val crossLibDependencies = Def.setting {
  Seq(
    Deps.scalatest % Test,
    Deps.sttp,
    Deps.ujson,
    Deps.pdfbox,
    Deps.poi,
    Deps.tika,
    Deps.requests,
    Deps.jsoup,
    Deps.postgres,
    Deps.config,
    Deps.hikariCP
  )
}

lazy val crossTestScala2 = (project in file("modules/crosstest/scala2"))
  .dependsOn(core)
  .settings(
    name         := "crosstest-scala2",
    scalaVersion := scala213,
    Test / fork  := true,
    resolvers   += Resolver.mavenLocal,
    resolvers   += Resolver.defaultLocal,
    libraryDependencies ++= crossLibDependencies.value
  )

lazy val crossTestScala3 = (project in file("modules/crosstest/scala3"))
  .dependsOn(core)
  .settings(
    name         := "crosstest-scala3",
    scalaVersion := scala3,
    Test / fork  := true,
    resolvers   += Resolver.mavenLocal,
    resolvers   += Resolver.defaultLocal,
    libraryDependencies ++= crossLibDependencies.value,
    scalacOptions ++= scala3CompilerOptions
  )

mimaPreviousArtifacts := Set(
  organization.value %% "llm4s" % "0.1.4"
)

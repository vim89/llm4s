val organization = "$organization$"
val version = "$version$"
val scalaVersion = "$scala_version$"
val llm4sVersion = "$llm4s_version$"
val munitVersion = "$munitVersion$"
val crossScalaVersions = Seq("2.13.12", "3.3.1")

lazy val root = (project in file("."))
  .settings(
    name := "$name$",
    publish / skip := true,
    libraryDependencies += Seq(
      "org.llm4s" %% "llm4s" % llm4sVersionlibraryDependencies,
      "org.scalameta" %% "munit" % munitVersion % Test
    )
  )

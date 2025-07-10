llm4s.g8: sbt template for [llm4s], Scala 3 & Scala 2 cross-compiled
=================
[![CI](https://github.com/llm4s/llm4s/actions/workflows/release.yml/badge.svg)](https://github.com/llm4s/llm4s/actions/workflows/release.yml)

A [Giter8][g8] template for a [Scala 3] / [Scala 2] cross-compiled project.

Template usage
--------------
Using `sbt`, do:
```
sbt new llm4s/llm4s.g8 --name=<name-of-your-project> --organization=<your.organization> --version=<0.1.0> --llm4sVersion=<0.10.0> --scalaVersion=<3.4.0>
```
in the folder where you want to clone the template.

Quickstart
----------
This template bootstraps a production-ready Scala project pre-configured to use the [LLM4S SDK](https://github.com/llm4s/llm4s).

Features
--------
- ✅ Preconfigured with `OpenAI` integration via `LLM4S`
- ✅ Tracing support via `org.llm4s.trace.Tracing`
- ✅ Includes `Main.scala` + `PromptExecutor` for quick CLI interaction
- ✅ Supports `sbt run`, `sbt test`, and format checking
- ✅ Production-ready directory layout, CI hooks, formatting, and more

Run the app
-----------
1. Export your OpenAI API key:
   ```bash
   export OPENAI_API_KEY=sk-xxxxxx
   ```

2. Create the project:
   ```bash
   sbt new file:///<full-path-to-directory>/llm4s/llm4s.g8 --name=my-llm4s-app
   ```

3. Run with default or custom prompt:
   ```bash
   sbt run
   sbt "run Explain what a Monad is in Scala"
   ```

4. Format, compile, test:
   ```bash
   sbt scalafmtAll
   sbt compile
   sbt test
   ```

Structure
---------
```
.
├── build.sbt
├── src/
│   ├── main/scala/Main.scala
│   └── main/scala/PromptExecutor.scala
├── .scalafmt.conf
├── .github/workflows/ci.yml
└── README.md
```

Template usage (Local)
----------------------
To use this template locally (e.g., during development or when unpublished), run:
```
sbt new file:///<full-path-to-directory>/llm4s/llm4s.g8 --name=<name-of-your-project> --organization=<your.organization> --version=<0.1.0> --llm4sVersion=<0.10.0> --scalaVersion=<3.4.0>
```
Note: Do not use `~` as  ~/.. is not resolved correctly when passed to sbt new or other CLI tools — it must be expanded manually


----------------
Written in July 2025 by [Vitthal Mirji]

[g8]: http://www.foundweekends.org/giter8/
[llm4s]: https://github.com/llm4s/llm4s
[Scala 3]: https://dotty.epfl.ch/
[Scala 2]: https://www.scala-lang.org/
[Vitthal Mirji]: https://github.com/vim89
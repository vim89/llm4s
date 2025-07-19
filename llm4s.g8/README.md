llm4s.g8: sbt template for [llm4s], Scala 3 & Scala 2 cross-compiled
=================
[![CI](https://github.com/llm4s/llm4s/actions/workflows/release.yml/badge.svg)](https://github.com/llm4s/llm4s/actions/workflows/release.yml)

A [Giter8][g8] template for a [Scala 3] / [Scala 2] cross-compiled project.

Quickstart
----------
This template bootstraps a production-ready Scala project pre-configured to use the [LLM4S SDK](https://github.com/llm4s/llm4s).

Features
--------
- ✅ Preconfigured with `OpenAI` integration via `LLM4S`
- ✅ Includes `Main.scala` + `PromptExecutor` for quick CLI interaction
- ✅ Supports giter8 project creation using `sbt new`,
- ✅ Production-ready directory layout, CI hooks, formatting, logger/logging, pre-configured test framework and more

Template usage: to create a project
--------------
Using `sbt`, do:
```
sbt new llm4s/llm4s.g8 \
--name=llm4s-template \
--package=org.llm4s.template \
--version=0.1.0-SNAPSHOT \
--llm4s_version=0.1.1 \
--scala_version=2.13.16 \
--munit_version=1.1.1 \
--directory=org.llm4s.template \
--force
```
in the folder where you want to clone the template.

Template usage (local): to create a project
--------------
To use this template locally (e.g., during development or when unpublished),
Using `sbt`, do:
```
sbt new file:///<absolute-path>/llm4s/llm4s.g8 \
--name=llm4s-template \
--package=org.llm4s.template \
--version=0.1.0-SNAPSHOT \
--llm4s_version=0.1.1 \
--scala_version=2.13.16 \
--munit_version=1.1.1 \
--directory=org.llm4s.template \
--force
```
Note: Do not use `~` as  ~/.. is not resolved correctly when passed to sbt new or other CLI tools — it must be expanded manually

Run the app
-----------
1. Export your OpenAI API key:
   ```bash
   export OPENAI_API_KEY=sk-xxxxxx
   ```

2. Switch to the project directory:
   ```bash
   cd llm4s-template
   ls -lta
   ```
   Structure should look like:
   ```text
   <pre>
   .  
   ├── .github  
   │   └── workflows  
   │       └── ci.yml  
   ├── .gitignore  
   ├── .pre-commit-config.yaml  
   ├── .pre-commit-hooks.yaml  
   ├── .scalafmt.conf  
   ├── build.sbt  
   ├── project  
   │   ├── build.properties  
   │   └── plugins.sbt  
   ├── README.md  
   ├── src  
   │   ├── main  
   │   │   ├── resources  
   │   │   │   └── logback.xml  
   │   │   └── scala  
   │   │       └── org  
   │   │           └── llm4s  
   │   │               └── template  
   │   │                   ├── Main.scala  
   │   │                   └── PromptExecutor.scala  
   │   └── test  
   │       └── scala  
   │           └── org  
   │               └── llm4s  
   │                   └── template  
   │                       └── MainSpec.scala  
   </pre>
   ```


4. Format, compile, test:
   ```bash
   sbt scalafmtAll
   sbt compile
   sbt test
   ```
5. Run with default or custom prompt:
   ```bash
    sbt run
    sbt run "Explain what a Monad is in scala"
   ```
----------------
Written in July 2025 by [Vitthal Mirji]

[g8]: http://www.foundweekends.org/giter8/
[llm4s]: https://github.com/llm4s/llm4s
[Scala 3]: https://dotty.epfl.ch/
[Scala 2]: https://www.scala-lang.org/
[Vitthal Mirji]: https://github.com/vim89
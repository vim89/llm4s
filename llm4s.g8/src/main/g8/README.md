$name$
=================

Quickstart
----------
This is a production-ready scala project pre-configured to use the [llm4s SDK](https://github.com/llm4s/llm4s).

Features
--------
- ✅ Preconfigured with `llm4s` for building AI-powered applications
- ✅ Production-ready directory layout, CI hooks, formatting, and more
- ✅ Supports `sbt test` & `sbt run` for quick CLI interaction
- ✅ Supports & suites directly from the comfort of your IDE, whether it's IntelliJ, VS Code, or any other
- ✅ Includes `Main.scala` + `PromptExecutor` for quick onboarding & getting started with [llm4s]

Pre-configured prerequisites
-----------
- JDK 21+
- SBT
- OpenAI API key
- [Scala 3][Scala 3] or [Scala 2][Scala 2]
- [MUnit] for unit testing
- [LLM4S SDK][llm4s]
- Logging library [logback][logback], [scala-logging][scala-logging]

Run the app
-----------
1. Export your OpenAI API key:
   ```bash
   export OPENAI_API_KEY=sk-xxxxxx
   ```

2. Run with default or custom prompt:
   ```bash
   sbt run
   sbt run "Explain what a Monad is in Scala"
   ```

3. Format & compile (afte making any changes):
   ```bash
   sbt scalafmtAll
   sbt compile
   ```
4. Running Tests: This template comes with [MUnit](https://scalameta.org/munit/) preconfigured for testing.

- Included in this setup:
  - `munit` version `$munit_version$` is added as a test dependency in `build.sbt`. 
  - Sample test is located in: `src/test/scala/MainSpec.scala` that tests the `PromptExecutor` functionality.

- To run tests:
  - Use SBT:
  - ```bash 
    sbt test
    ```

[g8]: http://www.foundweekends.org/giter8/
[llm4s]: https://github.com/llm4s/llm4s
[Scala 3]: https://dotty.epfl.ch/
[Scala 2]: https://www.scala-lang.org/
[logback]: https://logback.qos.ch/
[scala-logging]: https://github.com/lightbend-labs/scala-logging
[MUnit]: https://scalameta.org/munit/

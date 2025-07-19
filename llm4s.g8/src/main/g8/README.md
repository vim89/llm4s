$name$
=================

Quickstart
----------
This is a production-ready scala project pre-configured to use the [llm4s SDK](https://github.com/llm4s/llm4s).

Features
--------
- ✅ Preconfigured with `OpenAI` integration via `LLM4S`
- ✅ Includes `Main.scala` + `PromptExecutor` for quick CLI interaction
- ✅ Supports `sbt run`, `sbt test`, and format checking
- ✅ Production-ready directory layout, CI hooks, formatting, and more

Run the app
-----------
1. Export your OpenAI API key:
   ```bash
   export OPENAI_API_KEY=sk-xxxxxx
   ```

2. Run with default or custom prompt:
   ```bash
   sbt run
   sbt "run Explain what a Monad is in Scala"
   ```

3. Format & compile (afte making any changes):
   ```bash
   sbt scalafmtAll
   sbt compile
   ```
4. Running Tests: This template comes with [MUnit](https://scalameta.org/munit/) preconfigured for testing.

- Included setup:
  - `munit` version `$munit_version$` is added as a test dependency. 
  - Sample test is located in: `src/test/scala/MainSpec.scala`

- To run tests:
  - Use SBT:
  - ```bash 
    sbt test
    ```

[g8]: http://www.foundweekends.org/giter8/
[llm4s]: https://github.com/llm4s/llm4s
[Scala 3]: https://dotty.epfl.ch/
[Scala 2]: https://www.scala-lang.org/
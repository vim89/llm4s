$name$
=================

Quickstart
----------
This is a production-ready Scala project pre-configured to use the [llm4s SDK](https://github.com/llm4s/llm4s).

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


[g8]: http://www.foundweekends.org/giter8/
[llm4s]: https://github.com/llm4s/llm4s
[Scala 3]: https://dotty.epfl.ch/
[Scala 2]: https://www.scala-lang.org/
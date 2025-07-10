import org.llm4s.openai.OpenAI
import org.llm4s.trace.Tracing

object PromptExecutor {
  def run(prompt: String): Unit = {
    val tracer = Tracing.create()
    tracer.traceEvent(s"Executing prompt: " + prompt)

    val model = OpenAI.fromEnv()
    val response = model.chat(prompt)

    println(s"LLM Response:\n" + response.content)
    tracer.traceCompletion(response.content, model)

    response.usage.foreach { usage =>
      tracer.traceTokenUsage(usage, model, "chat-completion")
    }

    tracer.traceEvent("Prompt execution finished")
  }
}

object Main {
  def main(args: Array[String]): Unit = {
    val prompt = args.headOption.getOrElse("Explain what a Monad is in Scala")
    PromptExecutor.run(prompt)
  }
}

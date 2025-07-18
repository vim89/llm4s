package $package;format="package"$

/**
 * This code is part of the Giter8 template llm4s.g8 in llm4s project, which provides a set standard template/archetype
 * for improve developer onboarding, creating new projects using the llm4s library.
 */
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

package org.llm4s.template


import org.llm4s.llmconnect.LLM
import org.llm4s.llmconnect.config.OpenAIConfig
import org.llm4s.llmconnect.model.{Conversation, SystemMessage, UserMessage}
import org.llm4s.llmconnect.provider.LLMProvider

/**
 * This code is part of the Giter8 template llm4s.g8 in llm4s project, which provides a set standard template/archetype
 * for improve developer onboarding, creating new projects using the llm4s library.
 */
object PromptExecutor {

  def run(prompt: String): Boolean = {
    // Log the prompt execution
    // Create the provider config
    val config = OpenAIConfig(
      apiKey = sys.env.getOrElse("OPENAI_API_KEY", "your-api-key-here"),
      model = "gpt-3.5-turbo",
      baseUrl = sys.env.getOrElse("OPENAI_BASE_URL", "https://api.openai.com/v1")
    )

    // Build the client via LLM factory using provider enum
    val client = LLM.client(LLMProvider.OpenAI, config)

    // Build a conversation
    val conversation = Conversation(Seq(
      SystemMessage("You are a helpful assistant."),
      UserMessage(prompt)
    ))

    // Perform synchronous completion
    client.complete(conversation) match {
      case Right(completion) =>
        println("✅ Assistant response: " + completion.message.content)
        true
      case Left(err) =>
        println("❌ Error: " + err)
        false
    }
  }
}

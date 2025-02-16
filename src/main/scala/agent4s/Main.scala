package agent4s

import agent4s.llmconnect.{LLMConnect, LLMConnection}
import com.azure.ai.openai.models.{
  ChatCompletionsOptions,
  ChatRequestAssistantMessage,
  ChatRequestMessage,
  ChatRequestSystemMessage,
  ChatRequestUserMessage
}
import com.azure.ai.openai.{OpenAIClient, OpenAIClientBuilder, OpenAIServiceVersion}
import com.azure.core.credential.{AzureKeyCredential, TokenCredential}

object Main {
  def main(args: Array[String]): Unit = {

    val llmConnection = LLMConnect.getClient()
    val client        = llmConnection.client

    val chatMessages = new java.util.ArrayList[ChatRequestMessage]
    chatMessages.add(new ChatRequestSystemMessage("You are a helpful assistant. You will talk like a pirate."))
    chatMessages.add(new ChatRequestUserMessage("Can you help me?"))
    chatMessages.add(new ChatRequestAssistantMessage("Of course, me hearty! What can I do for ye?"))
    chatMessages.add(new ChatRequestAssistantMessage("What's the best way to train a parrot?"))

    val chatCompletions =
      client.getChatCompletions(llmConnection.defaultModel, new ChatCompletionsOptions(chatMessages))

    System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId, chatCompletions.getCreatedAt)
    import scala.jdk.CollectionConverters._
    for (choice <- chatCompletions.getChoices.asScala) {
      val message = choice.getMessage
      System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex, message.getRole)
      System.out.println("Message:")
      System.out.println(message.getContent)
    }
  }

}

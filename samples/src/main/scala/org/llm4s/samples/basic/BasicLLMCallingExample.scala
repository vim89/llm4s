package org.llm4s.samples.basic

import com.azure.ai.openai.models._
import org.llm4s.llmconnect.LLMConnect

object BasicLLMCallingExample {
  def main(args: Array[String]): Unit = {

    val llmConnection = LLMConnect.getClient()
    val client        = llmConnection.client

    val chatMessages = new java.util.ArrayList[ChatRequestMessage]
    chatMessages.add(new ChatRequestSystemMessage("You are a helpful assistant. You will talk like a pirate."))
    chatMessages.add(new ChatRequestUserMessage("Please write a scala function to add two integers"))
    chatMessages.add(new ChatRequestAssistantMessage("Of course, me hearty! What can I do for ye?"))
    chatMessages.add(new ChatRequestUserMessage("What's the best way to train a parrot?"))

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

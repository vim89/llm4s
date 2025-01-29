package agent4s

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
  val model = Option(sys.env("LITELLM_MODEL"))
    .getOrElse(throw new IllegalArgumentException("LITELLM_MODEL not set"))
    .replace("azure/", "")
  def main(args: Array[String]): Unit = {

    val client       = createAzureClient()
    val chatMessages = new java.util.ArrayList[ChatRequestMessage]
    chatMessages.add(new ChatRequestSystemMessage("You are a helpful assistant. You will talk like a pirate."))
    chatMessages.add(new ChatRequestUserMessage("Can you help me?"))
    chatMessages.add(new ChatRequestAssistantMessage("Of course, me hearty! What can I do for ye?"))
    chatMessages.add(new ChatRequestAssistantMessage("What's the best way to train a parrot?"))

    val chatCompletions = client.getChatCompletions(model, new ChatCompletionsOptions(chatMessages))

    System.out.printf("Model ID=%s is created at %s.%n", chatCompletions.getId, chatCompletions.getCreatedAt)
    import scala.jdk.CollectionConverters._
    for (choice <- chatCompletions.getChoices.asScala) {
      val message = choice.getMessage
      System.out.printf("Index: %d, Chat Role: %s.%n", choice.getIndex, message.getRole)
      System.out.println("Message:")
      System.out.println(message.getContent)
    }
  }

  private def createAzureClient() = {

    // read environment variables - AZURE_API_KEY AZURE_API_VERSION AZURE_API_BASE
    val key = Option(sys.env("AZURE_API_KEY")).getOrElse(throw new IllegalArgumentException("AZURE_API_KEY not set"))
    val version =
      Option(sys.env("AZURE_API_VERSION")).getOrElse(throw new IllegalArgumentException("AZURE_API_KEY not set"))
    val base = Option(sys.env("AZURE_API_BASE")).getOrElse(throw new IllegalArgumentException("AZURE_API_KEY not set"))

    // create Azure client
    new OpenAIClientBuilder()
      .credential(new AzureKeyCredential(key))
      .endpoint(base)
      .serviceVersion(OpenAIServiceVersion.getLatest)
      .buildClient();

  }
}

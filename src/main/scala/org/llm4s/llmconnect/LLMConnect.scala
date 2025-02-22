package org.llm4s.llmconnect

import com.azure.ai.openai.{OpenAIClient, OpenAIClientBuilder, OpenAIServiceVersion}
import com.azure.core.credential.AzureKeyCredential

object LLMConnect {
  sealed trait ClientType
  case object OpenAIClient extends ClientType
  case object AzureClient  extends ClientType

  private def readEnv(key: String): Option[String] = {
    sys.env.get(key)
  }

  def getLLMModel(): String = {

    readEnv("LLM_MODEL").getOrElse(
      throw new IllegalArgumentException("LLM_MODEL not set, set this to define default model")
    )
  }

  def getClient(): LLMConnection = {

    val model = getLLMModel()
    println("Model: " + model)
    if (model.startsWith("openai/")) {
      val openaiClient = createOpenAIClient()
      LLMConnection(model.replace("openai/", ""), OpenAIClient, openaiClient)
    } else if (model.startsWith("azure/")) {
      val azureClient = createAzureClient()
      LLMConnection(model.replace("azure/", ""), AzureClient, azureClient)
    } else {
      throw new IllegalArgumentException(s"Model $model not supported")
    }
  }

  private def createOpenAIClient(): OpenAIClient = {
    val key = readEnv("OPENAI_API_KEY").getOrElse(
      throw new IllegalArgumentException("OPENAI_API_KEY not set, required when using openai/ model.")
    )
    new OpenAIClientBuilder().credential(new AzureKeyCredential(key)).buildClient();
  }
  private def createAzureClient(): OpenAIClient = {

    // read environment variables - AZURE_API_KEY AZURE_API_VERSION AZURE_API_BASE
    val key = Option(sys.env("AZURE_API_KEY"))
      .getOrElse(throw new IllegalArgumentException("AZURE_API_KEY not set, required when using azure/ model."))
    val version =
      Option(sys.env("AZURE_API_VERSION")).getOrElse(
        throw new IllegalArgumentException(
          s"AZURE_API_KEY not set, required when using azure/ model. Options - ${OpenAIServiceVersion.values().map(_.toString).mkString(",")}."
        )
      )
    val base = Option(sys.env("AZURE_API_BASE"))
      .getOrElse(throw new IllegalArgumentException("AZURE_API_KEY not set, , required when using azure/ model."))

    // create Azure client
    new OpenAIClientBuilder()
      .credential(new AzureKeyCredential(key))
      .endpoint(base)
      .serviceVersion(OpenAIServiceVersion.valueOf(version))
      .buildClient();

  }

}

case class LLMConnection(defaultModel: String, clientType: LLMConnect.ClientType, client: OpenAIClient)

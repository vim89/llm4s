package org.llm4s.llmconnect

import com.azure.ai.openai.{ OpenAIClientBuilder, OpenAIServiceVersion, OpenAIClient => AzureOpenAIClient }
import com.azure.core.credential.AzureKeyCredential
import org.llm4s.config.EnvLoader
import org.llm4s.llmconnect.config.{ AnthropicConfig, AzureConfig, OpenAIConfig, ProviderConfig }
import org.llm4s.llmconnect.model._
import org.llm4s.llmconnect.provider.{
  AnthropicClient,
  LLMProvider,
  OpenAIClient,
  OpenRouterClient,
  AzureOpenAIClient => AzureClient
}

object LLMConnect {
  private def readEnv(key: String): Option[String] =
    EnvLoader.get(key)

  /**
   * Get an LLMClient based on environment variables
   */
  def getClient(): LLMClient = {
    val LLM_MODEL_ENV_KEY = "LLM_MODEL"
    val model = readEnv(LLM_MODEL_ENV_KEY).getOrElse(
      throw new IllegalArgumentException(
        s"Please set the `$LLM_MODEL_ENV_KEY` environment variable to specify the default model"
      )
    )

    // Always use OpenRouterClient if OPENAI_BASE_URL contains 'openrouter.ai'
    val openaiBaseUrl = readEnv("OPENAI_BASE_URL").getOrElse("https://api.openai.com/v1")
    if (openaiBaseUrl.contains("openrouter.ai")) {
      val modelName = if (model.startsWith("openai/")) model.replace("openai/", "") else model
      val config    = OpenAIConfig.fromEnv(modelName)
      new OpenRouterClient(config)
    } else if (model.startsWith("openai/")) {
      val modelName = model.replace("openai/", "")
      val config    = OpenAIConfig.fromEnv(modelName)
      new OpenAIClient(config)
    } else if (model.startsWith("openrouter/")) {
      val modelName = model.replace("openrouter/", "")
      val config    = OpenAIConfig.fromEnv(modelName)
      new OpenRouterClient(config)
    } else if (model.startsWith("azure/")) {
      val modelName   = model.replace("azure/", "")
      val config      = AzureConfig.fromEnv(modelName)
      val azureClient = createAzureClient(config)
      new AzureClient(config, azureClient)
    } else if (model.startsWith("anthropic/")) {
      val modelName = model.replace("anthropic/", "")
      val config    = AnthropicConfig.fromEnv(modelName)
      new AnthropicClient(config)
    } else {
      throw new IllegalArgumentException(
        s"Model $model is not supported. Supported formats are: 'openai/model-name', 'openrouter/model-name', 'azure/model-name', or 'anthropic/model-name'."
      )
    }
  }

  /**
   * Get an LLMClient with explicit provider and configuration
   */
  def getClient(provider: LLMProvider, config: ProviderConfig): LLMClient =
    provider match {
      case LLMProvider.OpenAI =>
        new OpenAIClient(config.asInstanceOf[OpenAIConfig])
      case LLMProvider.OpenRouter =>
        new OpenRouterClient(config.asInstanceOf[OpenAIConfig])
      case LLMProvider.Azure =>
        val azureConfig = config.asInstanceOf[AzureConfig]
        val azureClient = createAzureClient(azureConfig)
        new AzureClient(azureConfig, azureClient)
      case LLMProvider.Anthropic =>
        val anthropicConfig = config.asInstanceOf[AnthropicConfig]
        new AnthropicClient(anthropicConfig)
    }

  private def createAzureClient(config: AzureConfig): AzureOpenAIClient =
    new OpenAIClientBuilder()
      .credential(new AzureKeyCredential(config.apiKey))
      .endpoint(config.endpoint)
      .serviceVersion(OpenAIServiceVersion.valueOf(config.apiVersion))
      .buildClient()

  /**
   * Convenience method for quick completion
   */
  def complete(
    messages: Seq[Message],
    options: CompletionOptions = CompletionOptions()
  ): Either[LLMError, Completion] = {
    val conversation = Conversation(messages)
    getClient().complete(conversation, options)
  }
}

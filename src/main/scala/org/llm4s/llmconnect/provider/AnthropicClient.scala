package org.llm4s.llmconnect.provider

import com.anthropic.client.okhttp.AnthropicOkHttpClient
import com.anthropic.core.{ JsonValue, ObjectMappers }
import com.anthropic.models.{ MessageCreateParams, Tool }
import org.llm4s.llmconnect.LLMClient
import org.llm4s.llmconnect.config.AnthropicConfig
import org.llm4s.llmconnect.model._
import org.llm4s.toolapi.ToolFunction

import scala.jdk.CollectionConverters._

class AnthropicClient(config: AnthropicConfig) extends LLMClient {
  // Initialize Anthropic client
  private val client = AnthropicOkHttpClient
    .builder()
    .apiKey(config.apiKey)
    .baseUrl(config.baseUrl)
    .build()

  override def complete(
    conversation: Conversation,
    options: CompletionOptions
  ): Either[LLMError, Completion] =
    try {
      // Create message parameters builder
      val paramsBuilder = MessageCreateParams
        .builder()
        .model(config.model)
        .temperature(options.temperature.floatValue())
        .topP(options.topP.floatValue())

      // Add max tokens if specified
      // max tokens is required by the api
      val maxTokens = options.maxTokens.getOrElse(2048)
      paramsBuilder.maxTokens(maxTokens)

      // Add tools if specified
      if (options.tools.nonEmpty) {
        options.tools.foreach(tool => paramsBuilder.addTool(convertToolToAnthropicTool(tool)))
      }

      // Add messages from conversation
      addMessagesToParams(conversation, paramsBuilder)

      // Build the parameters
      val messageParams = paramsBuilder.build()

      // Make API call
      val response = client.messages().create(messageParams)

      // Convert response to our model
      Right(convertFromAnthropicResponse(response))
    } catch {
      case e: com.anthropic.errors.UnauthorizedException =>
        Left(AuthenticationError(e.getMessage))
      case e: com.anthropic.errors.RateLimitException =>
        Left(RateLimitError(e.getMessage))
      case e: com.anthropic.errors.AnthropicInvalidDataException =>
        Left(ValidationError(e.getMessage))
      case e: Exception =>
        Left(UnknownError(e))
    }

  override def streamComplete(
    conversation: Conversation,
    options: CompletionOptions = CompletionOptions(),
    onChunk: StreamedChunk => Unit
  ): Either[LLMError, Completion] =
    throw new NotImplementedError("Streaming with anthropic not supported")

  // Add messages from conversation to the parameters builder
  private def addMessagesToParams(
    conversation: Conversation,
    paramsBuilder: MessageCreateParams.Builder
  ): Unit = {
    // Track if we've seen a system message
    var hasSystemMessage = false

    // Process messages in order
    conversation.messages.foreach {
      case SystemMessage(content) =>
        paramsBuilder.system(content)
        hasSystemMessage = true

      case UserMessage(content) =>
        paramsBuilder.addUserMessage(content)

      case AssistantMessage(content, _) =>
        paramsBuilder.addAssistantMessage(content)

      case ToolMessage(toolCallId, content) =>
        // Anthropic doesn't have a direct equivalent to tool messages
        // We'll add it as a user message with a prefix
        paramsBuilder.addUserMessage(s"Tool result for $toolCallId: $content")
    }

    // Add a default system message if none was provided
    if (!hasSystemMessage) {
      paramsBuilder.system("You are Claude, a helpful AI assistant.")
    }
  }

  // Convert our ToolFunction to Anthropic's Tool
  private def convertToolToAnthropicTool(toolFunction: ToolFunction[_, _]): Tool = {
    val schema     = toolFunction.schema
    val jsonSchema = schema.toJsonSchema

    // Convert to Anthropic InputSchema format
    val inputSchemaBuilder = Tool.InputSchema.builder()

    // Add properties
    println("JS = \n" + jsonSchema.obj("properties").render(indent = 2))
    inputSchemaBuilder.properties(JsonValue.from(jsonSchema.obj("properties").render()))

    // Add required fields if present
    if (jsonSchema.obj.contains("required")) {
      inputSchemaBuilder.putAdditionalProperty("required", JsonValue.from(jsonSchema.obj("required").render()))
    }
    // Build the tool
    Tool
      .builder()
      .name(toolFunction.name)
      .description(toolFunction.description)
      .inputSchema(inputSchemaBuilder.build())
      .build()
  }

  // Convert Anthropic response to our model
  private def convertFromAnthropicResponse(response: com.anthropic.models.Message): Completion = {
    // Extract content
    val content = response
      .content()
      .asScala
      .toList
      .filter(_.isText)
      .map(_.asText().text())
      .mkString

    // Extract tool calls if present
    val toolCalls = extractToolCalls(response)

    // Create completion
    Completion(
      id = response.id(),
      created = System.currentTimeMillis() / 1000, // Use current time as created timestamp
      message = AssistantMessage(
        content = content,
        toolCalls = toolCalls
      ),
      usage = Some(
        TokenUsage(
          promptTokens = response.usage().inputTokens().toInt,
          completionTokens = response.usage().outputTokens().toInt,
          totalTokens = (response.usage().inputTokens() + response.usage().outputTokens()).toInt
        )
      )
    )
  }

  // Extract tool calls from Anthropic response
  private def extractToolCalls(response: com.anthropic.models.Message): Seq[ToolCall] = {
    val toolCalls = response.content().asScala.toList.filter(_.isToolUse).map { cb =>
      val toolUse   = cb.asToolUse()
      val toolId    = toolUse.id()
      val toolName  = toolUse.name()
      val rawParams = toolUse._input()
      val arguments = ujson.read(ObjectMappers.jsonMapper().writeValueAsString(rawParams))

      ToolCall(
        id = toolId,
        name = toolName,
        arguments = arguments
      )
    }

    toolCalls
  }
}

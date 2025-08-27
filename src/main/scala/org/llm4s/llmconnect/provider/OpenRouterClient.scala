package org.llm4s.llmconnect.provider

import org.llm4s.llmconnect.LLMClient
import org.llm4s.llmconnect.config.OpenAIConfig
import org.llm4s.llmconnect.model._
import org.llm4s.llmconnect.serialization.OpenRouterToolCallDeserializer
import org.llm4s.llmconnect.streaming.{ SSEParser, StreamingAccumulator }
import org.llm4s.toolapi.ToolRegistry
import org.llm4s.types.Result
import org.llm4s.error.{ LLMError, AuthenticationError, RateLimitError, ServiceError }

import java.net.URI
import java.net.http.{ HttpClient, HttpRequest, HttpResponse }
import java.time.Duration
import java.io.{ BufferedReader, InputStreamReader }
import java.nio.charset.StandardCharsets

class OpenRouterClient(config: OpenAIConfig) extends LLMClient {
  private val httpClient = HttpClient.newHttpClient()

  override def complete(
    conversation: Conversation,
    options: CompletionOptions
  ): Result[Completion] =
    try {
      // Convert conversation to OpenRouter format
      val requestBody = createRequestBody(conversation, options)

      // Make API call
      val request = HttpRequest
        .newBuilder()
        .uri(URI.create(s"${config.baseUrl}/chat/completions"))
        .header("Content-Type", "application/json")
        .header("Authorization", s"Bearer ${config.apiKey}")
        .header("HTTP-Referer", "https://github.com/llm4s/llm4s") // Required by OpenRouter
        .header("X-Title", "LLM4S")                               // Required by OpenRouter
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.render()))
        .build()

      val response = httpClient.send(request, HttpResponse.BodyHandlers.ofString())

      // Handle response status
      response.statusCode() match {
        case 200 =>
          // Parse successful response
          val responseJson = ujson.read(response.body())
          Right(parseCompletion(responseJson))

        case 401    => Left(AuthenticationError("openrouter", "Invalid API key"))
        case 429    => Left(RateLimitError("openrouter"))
        case status => Left(ServiceError(status, "openrouter", s"OpenRouter API error: ${response.body()}"))
      }
    } catch {
      case e: Exception => Left(LLMError.fromThrowable(e))
    }

  override def streamComplete(
    conversation: Conversation,
    options: CompletionOptions = CompletionOptions(),
    onChunk: StreamedChunk => Unit
  ): Result[Completion] =
    try {
      // Create request body with streaming enabled
      val requestBody = createRequestBody(conversation, options)
      requestBody("stream") = true

      // Make streaming API call
      val request = HttpRequest
        .newBuilder()
        .uri(URI.create(s"${config.baseUrl}/chat/completions"))
        .header("Content-Type", "application/json")
        .header("Authorization", s"Bearer ${config.apiKey}")
        .header("HTTP-Referer", "https://github.com/llm4s/llm4s") // Required by OpenRouter
        .header("X-Title", "LLM4S")                               // Required by OpenRouter
        .timeout(Duration.ofMinutes(5))
        .POST(HttpRequest.BodyPublishers.ofString(requestBody.render()))
        .build()

      // Send request and get streaming response
      val response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream())

      // Check response status
      if (response.statusCode() != 200) {
        val errorBody = new String(response.body().readAllBytes(), StandardCharsets.UTF_8)
        response.statusCode() match {
          case 401    => return Left(AuthenticationError("openrouter", "Invalid API key"))
          case 429    => return Left(RateLimitError("openrouter"))
          case status => return Left(ServiceError(status, "openrouter", s"OpenRouter API error: $errorBody"))
        }
      }

      // Create SSE parser and accumulator
      val sseParser   = SSEParser.createStreamingParser()
      val accumulator = StreamingAccumulator.create()
      val reader      = new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8))

      try {
        var line: String = null
        while ({ line = reader.readLine(); line != null }) {
          sseParser.addChunk(line + "\n")

          // Process any available events
          while (sseParser.hasEvents)
            sseParser.nextEvent().foreach { event =>
              event.data.foreach { data =>
                if (data == "[DONE]") {
                  // Stream complete
                } else {
                  // Parse OpenAI format chunk
                  val json  = ujson.read(data)
                  val chunk = parseStreamingChunk(json)
                  chunk.foreach { c =>
                    accumulator.addChunk(c)
                    onChunk(c)
                  }
                }
              }
            }
        }
      } finally {
        reader.close()
        response.body().close()
      }

      // Return the accumulated completion
      accumulator.toCompletion()
    } catch {
      case e: Exception => Left(LLMError.fromThrowable(e))
    }

  private def parseStreamingChunk(json: ujson.Value): Option[StreamedChunk] =
    try {
      val choices = json("choices").arr
      if (choices.nonEmpty) {
        val choice = choices(0)
        val delta  = choice("delta")

        val content      = delta.obj.get("content").flatMap(_.strOpt)
        val finishReason = choice.obj.get("finish_reason").flatMap(_.strOpt).filter(_ != "null")

        // Handle tool calls if present
        val toolCall = delta.obj.get("tool_calls").flatMap { toolCallsVal =>
          val toolCalls = toolCallsVal.arr
          if (toolCalls.nonEmpty) {
            val call = toolCalls(0)
            Some(
              ToolCall(
                id = call.obj.get("id").flatMap(_.strOpt).getOrElse(""),
                name = call.obj.get("function").flatMap(_("name").strOpt).getOrElse(""),
                arguments = call.obj
                  .get("function")
                  .flatMap(_("arguments").strOpt)
                  .map(args => ujson.read(args))
                  .getOrElse(ujson.Null)
              )
            )
          } else None
        }

        Some(
          StreamedChunk(
            id = json.obj.get("id").flatMap(_.strOpt).getOrElse(""),
            content = content,
            toolCall = toolCall,
            finishReason = finishReason
          )
        )
      } else {
        None
      }
    } catch {
      case _: Exception => None
    }

  private def createRequestBody(conversation: Conversation, options: CompletionOptions): ujson.Obj = {
    val messages = conversation.messages.map {
      case UserMessage(content) =>
        ujson.Obj("role" -> "user", "content" -> content)
      case SystemMessage(content) =>
        ujson.Obj("role" -> "system", "content" -> content)
      case AssistantMessage(content, toolCalls) =>
        val base = ujson.Obj("role" -> "assistant", "content" -> content)
        if (toolCalls.nonEmpty) {
          base("tool_calls") = ujson.Arr.from(toolCalls.map { tc =>
            ujson.Obj(
              "id"   -> tc.id,
              "type" -> "function",
              "function" -> ujson.Obj(
                "name"      -> tc.name,
                "arguments" -> tc.arguments.render()
              )
            )
          })
        }
        base
      case ToolMessage(toolCallId, content) =>
        ujson.Obj(
          "role"         -> "tool",
          "tool_call_id" -> toolCallId,
          "content"      -> content
        )
    }

    val base = ujson.Obj(
      "model"       -> config.model,
      "messages"    -> ujson.Arr.from(messages),
      "temperature" -> options.temperature,
      "top_p"       -> options.topP
    )

    options.maxTokens.foreach(mt => base("max_tokens") = mt)
    if (options.presencePenalty != 0) base("presence_penalty") = options.presencePenalty
    if (options.frequencyPenalty != 0) base("frequency_penalty") = options.frequencyPenalty

    if (options.tools.nonEmpty) {
      val toolRegistry = new ToolRegistry(options.tools)
      base("tools") = toolRegistry.getOpenAITools()
    }

    base
  }

  private def parseCompletion(json: ujson.Value): Completion = {
    val choice  = json("choices")(0)
    val message = choice("message")

    // Extract tool calls if present
    val toolCalls = Option(message.obj.get("tool_calls"))
      .map(tc => OpenRouterToolCallDeserializer.deserializeToolCalls(tc))
      .getOrElse(Seq.empty)

    val usage = Option(json.obj.get("usage")).flatMap { u =>
      val usageObjOpt =
        u.objOpt.orElse(u.arrOpt.flatMap(_.headOption.flatMap(_.objOpt)))
      usageObjOpt.map { usageObj =>
        TokenUsage(
          promptTokens = usageObj.value("prompt_tokens").num.toInt,
          completionTokens = usageObj.value("completion_tokens").num.toInt,
          totalTokens = usageObj.value("total_tokens").num.toInt
        )
      }
    }

    Completion(
      id = json("id").str,
      created = json("created").num.toLong,
      content = message.obj.get("content").flatMap(_.strOpt).getOrElse(""),
      model = json("model").str,
      message = AssistantMessage(
        contentOpt = message.obj.get("content").flatMap(_.strOpt),
        toolCalls = toolCalls.toList
      ),
      toolCalls = toolCalls.toList,
      usage = usage
    )
  }
}

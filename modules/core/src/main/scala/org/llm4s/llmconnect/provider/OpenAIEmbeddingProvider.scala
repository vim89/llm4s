package org.llm4s.llmconnect.provider

import org.llm4s.llmconnect.config.EmbeddingProviderConfig
import org.llm4s.llmconnect.model._
import org.slf4j.LoggerFactory
import sttp.client4._
import ujson.{ Arr, Obj, read }

import scala.util.Try

/**
 * OpenAI embedding provider implementation.
 *
 * Provides text embeddings using OpenAI's embedding API (text-embedding-3-small,
 * text-embedding-3-large, text-embedding-ada-002). Supports batch embedding of
 * multiple texts in a single request.
 *
 * == Supported Models ==
 *  - `text-embedding-3-small` - Efficient, lower cost (recommended)
 *  - `text-embedding-3-large` - Higher quality, higher cost
 *  - `text-embedding-ada-002` - Legacy model
 *
 * == Token Usage ==
 * The response includes token usage information when available from the API.
 *
 * @see [[EmbeddingProvider]] for the provider interface
 * @see [[org.llm4s.llmconnect.config.EmbeddingProviderConfig]] for configuration
 */
object OpenAIEmbeddingProvider {

  /**
   * Creates an OpenAI embedding provider from configuration.
   *
   * @param cfg embedding provider configuration with API key and base URL
   * @return configured EmbeddingProvider instance
   */
  def fromConfig(cfg: EmbeddingProviderConfig): EmbeddingProvider = new EmbeddingProvider {
    private val backend = DefaultSyncBackend()
    private val logger  = LoggerFactory.getLogger(getClass)

    override def embed(request: EmbeddingRequest): Either[EmbeddingError, EmbeddingResponse] = {
      val model = request.model.name
      val input = request.input
      val payload = Obj(
        "input" -> Arr.from(input),
        "model" -> model
      )

      val url = uri"${cfg.baseUrl}/v1/embeddings"
      logger.debug(s"[OpenAIEmbeddingProvider] POST $url model=$model inputs=${input.size}")

      val respEither: Either[EmbeddingError, Response[Either[String, String]]] =
        Try(
          basicRequest
            .post(url)
            .header("Authorization", s"Bearer ${cfg.apiKey}")
            .header("Content-Type", "application/json")
            .body(payload.render())
            .send(backend)
        ).toEither.left
          .map(e =>
            EmbeddingError(code = Some("502"), message = s"HTTP request failed: ${e.getMessage}", provider = "openai")
          )

      respEither.flatMap { response =>
        response.body match {
          case Right(body) =>
            Try {
              val json     = read(body)
              val vectors  = json("data").arr.map(r => r("embedding").arr.map(_.num).toVector).toSeq
              val metadata = Map("provider" -> "openai", "model" -> model, "count" -> input.size.toString)

              // Extract token usage if available
              val usage = json.obj.get("usage").flatMap { usageJson =>
                Try {
                  val promptTokens = usageJson("prompt_tokens").num.toInt
                  val totalTokens  = usageJson("total_tokens").num.toInt
                  EmbeddingUsage(promptTokens = promptTokens, totalTokens = totalTokens)
                }.toOption
              }

              EmbeddingResponse(embeddings = vectors, metadata = metadata, usage = usage)
            }.toEither.left
              .map { ex =>
                logger.error(s"[OpenAIEmbeddingProvider] Parse error: ${ex.getMessage}")
                EmbeddingError(code = Some("502"), message = s"Parsing error: ${ex.getMessage}", provider = "openai")
              }
          case Left(errorMsg) =>
            logger.error(s"[OpenAIEmbeddingProvider] HTTP error: $errorMsg")
            Left(EmbeddingError(code = Some("502"), message = errorMsg, provider = "openai"))
        }
      }
    }
  }
}

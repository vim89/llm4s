package org.llm4s.error

/**
 * Rate limiting errors - typically recoverable with exponential backoff
 */

sealed abstract class RateLimitError private (
  val message: String,
  val retryAfter: Option[Long],
  val provider: String
) extends LLMError
    with RecoverableError {
  override val context: Map[String, String] = Map("provider" -> provider) ++
    retryAfter.map(r => Map("retryAfter" -> r.toString)).getOrElse(Map.empty)
}

object RateLimitError {

  /** Create basic rate limit error */
  def apply(provider: String): RateLimitError =
    new RateLimitError(s"Rate limited by $provider", None, provider) {}

  /** Create rate limit error with retry delay */
  def apply(provider: String, retryAfter: Long): RateLimitError =
    new RateLimitError(s"Rate limited by $provider. Retry after $retryAfter seconds", Some(retryAfter), provider) {}

  /** Unapply extractor for pattern matching */
  def unapply(error: RateLimitError): Option[(String, Option[Long], String)] =
    Some((error.message, error.retryAfter, error.provider))
}

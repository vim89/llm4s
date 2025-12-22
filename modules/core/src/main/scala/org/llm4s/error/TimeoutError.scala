package org.llm4s.error

import scala.concurrent.duration.Duration

/**
 * Timeout errors that can potentially succeed with a different timeout or retry.
 *
 * Represents operations that exceeded their time limit. As a [[RecoverableError]],
 * these can be retried with longer timeouts or exponential backoff strategies.
 *
 * @param message Human-readable error description
 * @param timeoutDuration The timeout duration that was exceeded
 * @param operation The operation that timed out (e.g., "api-call", "completion")
 * @param cause Optional underlying exception
 * @param context Additional key-value context for debugging
 *
 * @example
 * {{{
 * import scala.concurrent.duration._
 *
 * val error = TimeoutError("Request timed out", 30.seconds, "api-call")
 *   .withContext("endpoint", "https://api.openai.com")
 * }}}
 */
final case class TimeoutError(
  message: String,
  timeoutDuration: Duration,
  operation: String,
  cause: Option[Throwable] = None,
  override val context: Map[String, String] = Map.empty
) extends LLMError
    with RecoverableError {

  /** Adds a single key-value pair to the error context. */
  def withContext(key: String, value: String): TimeoutError =
    copy(context = context + (key -> value))

  /** Adds multiple key-value pairs to the error context. */
  def withContext(entries: Map[String, String]): TimeoutError =
    copy(context = context ++ entries)

  /** Updates the operation and adds it to the context. */
  def withOperation(op: String): TimeoutError =
    copy(operation = op, context = context + ("operation" -> op))
}

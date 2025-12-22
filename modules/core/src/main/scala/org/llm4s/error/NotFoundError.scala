package org.llm4s.error

/**
 * Error indicating a required configuration or environment value was not found.
 *
 * Use this error when a required configuration key, environment variable, or
 * resource is missing. As a [[NonRecoverableError]], the operation cannot proceed
 * without providing the missing value.
 *
 * @param message Human-readable error description
 * @param key The configuration key or identifier that was not found
 *
 * @example
 * {{{
 * val error = NotFoundError("API key not found", "OPENAI_API_KEY")
 * error.context  // Map("key" -> "OPENAI_API_KEY")
 * }}}
 */
final case class NotFoundError(
  override val message: String,
  key: String
) extends LLMError
    with NonRecoverableError {
  override val context: Map[String, String] = Map("key" -> key)
}

package org.llm4s.core.safety

import scala.util.matching.Regex

/**
 * Utilities for redacting sensitive information from log messages.
 *
 * Automatically detects and masks common sensitive patterns including:
 *  - API keys (OpenAI, Anthropic, Google, etc.)
 *  - Bearer tokens and Authorization headers
 *  - URL query parameters with sensitive keys
 *  - Generic secrets and passwords
 *
 * @example
 * {{{
 * import org.llm4s.core.safety.LogRedaction
 *
 * val message = "Authorization: Bearer sk-abc123..."
 * val safe = LogRedaction.redact(message)
 * // Result: "Authorization: Bearer [REDACTED]"
 *
 * val url = "https://api.example.com?api_key=secret123"
 * val safeUrl = LogRedaction.redact(url)
 * // Result: "https://api.example.com?api_key=[REDACTED]"
 * }}}
 */
object LogRedaction {

  /**
   * Default redaction placeholder.
   */
  val RedactionPlaceholder: String = "[REDACTED]"

  /**
   * Patterns for sensitive URL query parameters.
   */
  private val SensitiveQueryParams: Set[String] = Set(
    "key",
    "api_key",
    "apikey",
    "api-key",
    "secret",
    "token",
    "access_token",
    "password",
    "passwd",
    "auth",
    "authorization",
    "credential",
    "credentials",
    "private_key",
    "secret_key"
  )

  /**
   * Pattern for query parameters.
   */
  private val QueryParamPattern: Regex = """([?&])([^=]+)=([^&\s]*)""".r

  /**
   * Pattern for JSON key-value pairs with sensitive keys.
   */
  private val SensitiveJsonKeys: Set[String] = Set(
    "api_key",
    "apiKey",
    "apikey",
    "api-key",
    "secret",
    "secretKey",
    "secret_key",
    "password",
    "passwd",
    "token",
    "accessToken",
    "access_token",
    "authorization",
    "credential",
    "credentials",
    "privateKey",
    "private_key"
  )

  /**
   * Pattern for JSON string values.
   */
  private def jsonKeyPattern(key: String): Regex =
    s"""(?i)("${Regex.quote(key)}"\\s*:\\s*")([^"]+)(")""".r

  /**
   * Redact sensitive information from a string.
   *
   * This method applies multiple redaction strategies:
   * 1. Known API key patterns
   * 2. Authorization headers
   * 3. Sensitive URL query parameters
   * 4. Sensitive JSON fields
   *
   * @param input The input string potentially containing sensitive data
   * @param placeholder The placeholder to use for redacted content
   * @return The input with sensitive data redacted
   */
  def redact(input: String, placeholder: String = RedactionPlaceholder): String =
    if (input == null || input.isEmpty) {
      input
    } else {
      var result = input

      // Redact Authorization headers first (most specific)
      result = redactAuthHeaders(result, placeholder)

      // Redact sensitive query parameters
      result = redactQueryParams(result, placeholder)

      // Redact sensitive JSON fields
      result = redactJsonFields(result, placeholder)

      // Redact known API key patterns
      result = redactApiKeys(result, placeholder)

      result
    }

  /**
   * Redact Authorization headers.
   */
  private def redactAuthHeaders(input: String, placeholder: String): String = {
    var result = input

    // Handle "Authorization": "..." in JSON
    result = """(?i)("Authorization"\s*:\s*")([^"]+)(")""".r
      .replaceAllIn(result, m => s"${m.group(1)}$placeholder${m.group(3)}")

    // Handle Authorization: ... in headers (capture everything until newline or end)
    // This handles "Authorization: Bearer xxx", "Authorization: Basic xxx", etc.
    result = """(?i)(Authorization:\s*)([^\n\r]+)""".r
      .replaceAllIn(result, m => s"${m.group(1)}$placeholder")

    // Handle standalone Bearer tokens (when not part of Authorization header)
    result = """(?i)\bBearer\s+([a-zA-Z0-9\-_\.]+)""".r
      .replaceAllIn(result, placeholder)

    // Handle standalone Basic auth tokens (when not part of Authorization header)
    result = """(?i)\bBasic\s+([a-zA-Z0-9+/=]+)""".r
      .replaceAllIn(result, placeholder)

    result
  }

  /**
   * Redact sensitive URL query parameters.
   */
  private def redactQueryParams(input: String, placeholder: String): String =
    QueryParamPattern.replaceAllIn(
      input,
      m => {
        val separator = m.group(1)
        val key       = m.group(2)
        // value is m.group(3) but we don't need it - we replace entire value

        if (SensitiveQueryParams.exists(s => key.toLowerCase.contains(s.toLowerCase))) {
          s"$separator$key=$placeholder"
        } else {
          m.matched
        }
      }
    )

  /**
   * Redact sensitive JSON fields.
   */
  private def redactJsonFields(input: String, placeholder: String): String = {
    var result = input

    SensitiveJsonKeys.foreach { key =>
      val pattern = jsonKeyPattern(key)
      result = pattern.replaceAllIn(result, m => s"${m.group(1)}$placeholder${m.group(3)}")
    }

    result
  }

  /**
   * Redact known API key patterns.
   */
  private def redactApiKeys(input: String, placeholder: String): String = {
    var result = input

    // OpenAI keys
    result = """sk-(?:proj-)?[a-zA-Z0-9]{20,}""".r.replaceAllIn(result, placeholder)

    // Anthropic keys
    result = """sk-ant-[a-zA-Z0-9\-]{20,}""".r.replaceAllIn(result, placeholder)

    // Google keys
    result = """AIza[a-zA-Z0-9_\-]{35,}""".r.replaceAllIn(result, placeholder)

    // Voyage keys
    result = """pa-[a-zA-Z0-9]{20,}""".r.replaceAllIn(result, placeholder)

    // Langfuse keys (minimum 10 chars to catch shorter keys too)
    result = """[ps]k-lf-[a-zA-Z0-9\-]{10,}""".r.replaceAllIn(result, placeholder)

    result
  }

  /**
   * Redact sensitive data suitable for logging request/response bodies.
   *
   * This is a more aggressive redaction that also truncates long content.
   *
   * @param input The input to redact
   * @param maxLength Maximum length of the output (0 = no limit)
   * @param placeholder Redaction placeholder
   * @return Redacted and potentially truncated string
   */
  def redactForLogging(
    input: String,
    maxLength: Int = 1000,
    placeholder: String = RedactionPlaceholder
  ): String = {
    val redacted = redact(input, placeholder)

    if (maxLength > 0 && redacted.length > maxLength) {
      redacted.take(maxLength) + s"... [truncated, ${redacted.length - maxLength} chars omitted]"
    } else {
      redacted
    }
  }

  /**
   * Create a safe string representation for logging.
   *
   * This method is designed to be used with SLF4J-style logging:
   * {{{
   * logger.debug("Request body: {}", LogRedaction.safe(requestBody))
   * }}}
   *
   * @param value The value to make safe for logging
   * @return A safe string representation
   */
  def safe(value: Any): String =
    value match {
      case null      => "null"
      case s: String => redactForLogging(s)
      case other     => redactForLogging(other.toString)
    }
}

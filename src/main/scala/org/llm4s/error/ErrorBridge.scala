package org.llm4s.error

import org.apache.hc.core5.http.HttpException
import org.llm4s.{ error, llmconnect }

/**
 * Compatibility bridge between old and new error types.
 * Allows gradual migration without breaking existing code.
 *
 * This bridge will be removed in later versions when migration is complete.
 */
object ErrorBridge {

  /**
   * Convert legacy error to enhanced core error
   */
  def toError(oldError: llmconnect.model.LLMError): error.LLMError = oldError match {
    case llmconnect.model.AuthenticationError(message) => error.AuthenticationError(message, "provider")
    case llmconnect.model.RateLimitError(provider)     => error.RateLimitError(provider)
    case llmconnect.model.ServiceError(message, code)  => error.ServiceError(code, "provider", message)
    case llmconnect.model.ValidationError(message)     => error.ValidationError("", message)
    case llmconnect.model.UnknownError(throwable)      => error.UnknownError("Unknow error", throwable)
    case llmconnect.model.ProcessingError(message)     => error.ValidationError("", message)
    case llmconnect.model.InvalidInput(message)        => error.ValidationError("", message)
    case llmconnect.model.APIError(message) => error.NetworkError(message, Some(new HttpException(message)), "endpoint")
  }

  /**
   * Convert enhanced core error to legacy error (for backward compatibility)
   */
  def toLegacy(coreError: error.LLMError): llmconnect.model.LLMError = coreError match {
    case error.AuthenticationError(message, _, _) =>
      llmconnect.model.AuthenticationError(message)
    case error.RateLimitError(message, _, _) =>
      llmconnect.model.RateLimitError(message)
    case error.ServiceError(message, code, _, _) =>
      llmconnect.model.ServiceError(message, code)
    case error.ValidationError(message, _, _) =>
      llmconnect.model.ValidationError(message)
    case error.NetworkError(message, _, _) =>
      llmconnect.model.UnknownError(new java.net.ConnectException(message))
    case error.UnknownError(_, cause) =>
      llmconnect.model.UnknownError(cause)
    case other =>
      llmconnect.model.UnknownError(new RuntimeException(other.message))
  }
}

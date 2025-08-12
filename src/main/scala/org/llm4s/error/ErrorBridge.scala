package org.llm4s.error

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
  def toCore(oldError: llmconnect.model.LLMError, provider: String, code: Int, reason: String): error.LLMError = oldError match {
    case llmconnect.model.RateLimitError(message) =>
      error.LLMError.rateLimited(message)

    case llmconnect.model.UnknownError(throwable) =>
      error.LLMError.fromThrowable(throwable)
  }

  def toCore(oldError: llmconnect.model.LLMError, provider_or_reason: String): LLMError = oldError match {
    case llmconnect.model.AuthenticationError(message) => error.LLMError.authenticationFailed(provider_or_reason, message)
    case llmconnect.model.InvalidInput(message) => error.ValidationError(message, provider_or_reason)
    case llmconnect.model.ValidationError(message) => error.ValidationError(message, provider_or_reason)
  }

  def toCore(oldError: llmconnect.model.LLMError, provider: String, code: Int): ServiceError = oldError match {
    case llmconnect.model.ServiceError(message, code) =>
      error.ServiceError(details = message, httpStatus = code, provider= provider)

    case llmconnect.model.ProcessingError(message) =>
      error.ServiceError(details = message, httpStatus = code, provider= provider)

    case llmconnect.model.APIError(message) =>
      error.ServiceError(details = message, httpStatus = code, provider= provider)
  }

  /**
   * Convert enhanced core error to legacy error (for backward compatibility)
   */
  def toLegacy(coreError: error.LLMError): llmconnect.model.LLMError = coreError match {
    case error.AuthenticationError(message, _, _) =>
      llmconnect.model.AuthenticationError(message)
    case error.RateLimitError(message, _, _) =>
      llmconnect.model.RateLimitError(message)
    case error.ServiceError(serviceError: Option[(String, Int, String, Option[String])]) =>
      serviceError.map(se => llmconnect.model.ServiceError(se._1, se._2)).getOrElse(llmconnect.model.ServiceError("Unknown service error", -1))
    case error.ValidationError(validationError: Option[(String, Int, String, Option[String])]) =>
      validationError.map(se => llmconnect.model.ValidationError(se._1)).getOrElse(llmconnect.model.ValidationError("Unknown validation error"))
    case error.NetworkError(networkError: NetworkError) =>
      networkError.cause.map(t => llmconnect.model.UnknownError(t)).getOrElse(llmconnect.model.UnknownError(new java.net.ConnectException))
    case error.UnknownError(message, cause) =>
      llmconnect.model.UnknownError(cause)
    case other =>
      llmconnect.model.UnknownError(new RuntimeException(other.message))
  }
}

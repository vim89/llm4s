package org.llm4s.error

/**
 * Execution errors that might succeed on retry.
 *
 * Represents errors from command or process execution that may be transient,
 * such as temporary resource unavailability or intermittent failures.
 * As a [[RecoverableError]], retry strategies can be applied.
 *
 * @param message Human-readable error description
 * @param operation The operation that failed (e.g., "bash-script", "api-call")
 * @param exitCode Optional process exit code if applicable
 * @param cause Optional underlying exception
 * @param context Additional key-value context for debugging
 *
 * @example
 * {{{
 * val error = ExecutionError("Command failed", "bash-script")
 *   .withExitCode(1)
 *   .withContext("command", "npm install")
 * }}}
 */
final case class ExecutionError(
  message: String,
  operation: String,
  exitCode: Option[Int] = None,
  cause: Option[Throwable] = None,
  override val context: Map[String, String] = Map.empty
) extends LLMError
    with RecoverableError {

  /** Adds a single key-value pair to the error context. */
  def withContext(key: String, value: String): ExecutionError =
    copy(context = context + (key -> value))

  /** Adds multiple key-value pairs to the error context. */
  def withContext(entries: Map[String, String]): ExecutionError =
    copy(context = context ++ entries)

  /** Sets the exit code and adds it to the context. */
  def withExitCode(code: Int): ExecutionError =
    copy(exitCode = Some(code), context = context + ("exitCode" -> code.toString))
}

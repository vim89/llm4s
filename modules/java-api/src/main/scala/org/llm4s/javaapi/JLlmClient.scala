package org.llm4s.javaapi

import org.llm4s.core.safety.Safety
import org.llm4s.error.ValidationError
import org.llm4s.llmconnect.LLMClient
import org.llm4s.llmconnect.model.{ Completion, CompletionOptions, Conversation, UserMessage }
import org.llm4s.types.Result

/**
 * Java-friendly wrapper around [[LLMClient]].
 *
 * The underlying Scala client returns `Result[Completion]`; this wrapper
 * unwraps the content string and surfaces it as [[LlmResult]] so Java
 * callers never import any Scala types.
 *
 * Obtain instances via [[Llm4s.createDefaultClient]] or
 * [[Llm4s.createClient]].
 *
 * {{{
 * LlmResult<String> r = client.complete("What is 2+2?");
 * r.ifSuccess(System.out::println).ifFailure(e -> System.err.println(e.getMessage()));
 * }}}
 */
final class JLlmClient private[javaapi] (private[javaapi] val underlying: LLMClient) extends AutoCloseable {

  /**
   * Sends a single user query and returns the assistant's text response. A `null` query yields a
   * failed [[LlmResult]], never an exception.
   */
  def complete(query: String): LlmResult[String] =
    if (query == null) nullArgument("query")
    else guarded(underlying.complete(Conversation(Seq(UserMessage(query)))))

  /** Sends a pre-built conversation and returns the assistant's text response. */
  def complete(conversation: Conversation): LlmResult[String] =
    if (conversation == null) nullArgument("conversation")
    else guarded(underlying.complete(conversation))

  /**
   * Full access: send a conversation with explicit [[CompletionOptions]],
   * returning the raw text content.
   */
  def complete(conversation: Conversation, options: CompletionOptions): LlmResult[String] =
    if (conversation == null) nullArgument("conversation")
    else if (options == null) nullArgument("options")
    else guarded(underlying.complete(conversation, options))

  private def nullArgument(name: String): LlmResult[String] =
    LlmResult.failure(ValidationError.required(name))

  /**
   * The wrapper's contract is that it never throws for a failed call: an exception escaping the
   * underlying client (a provider bug, a serialization error) becomes a failed result carrying it
   * as the cause. `InterruptedException` is not captured, so thread interruption still propagates.
   */
  private def guarded(call: => Result[Completion]): LlmResult[String] =
    LlmResult.from(Safety.safely(call).flatMap(identity).map(_.content))

  override def close(): Unit = underlying.close()
}

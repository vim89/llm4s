package org.llm4s.trace

import org.llm4s.agent.AgentState
import org.llm4s.llmconnect.model.{ Completion, TokenUsage }
import org.llm4s.llmconnect.config.TracingSettings

/**
 * Legacy tracing interface that returns `Unit` instead of `Result[Unit]`.
 *
 * This interface is deprecated in favor of the new [[Tracing]] trait which
 * provides functional error handling via `Result[Unit]`.
 *
 * == Migration ==
 *
 * Replace usages of `LegacyTracing` with [[Tracing]]:
 *
 * {{{
 * // Old (deprecated)
 * val tracing: LegacyTracing = ...
 * tracing.traceEvent("event") // throws on error
 *
 * // New (recommended)
 * val tracing: Tracing = new ConsoleTracing()
 * tracing.traceEvent(TraceEvent.CustomEvent("event", ujson.Obj())) // returns Result[Unit]
 * }}}
 *
 * @deprecated Use [[Tracing]] instead for functional error handling
 */
@deprecated("Use Tracing trait instead", since = "0.5.0")
trait LegacyTracing {

  /** @deprecated Use Tracing.traceEvent(TraceEvent) instead */
  def traceEvent(event: String): Unit

  /** @deprecated Use Tracing.traceAgentState instead */
  def traceAgentState(state: AgentState): Unit

  /** @deprecated Use Tracing.traceToolCall instead */
  def traceToolCall(toolName: String, input: String, output: String): Unit

  /** @deprecated Use Tracing.traceError instead */
  def traceError(error: Throwable): Unit

  /** @deprecated Use Tracing.traceCompletion instead */
  def traceCompletion(completion: Completion, model: String): Unit

  /** @deprecated Use Tracing.traceTokenUsage instead */
  def traceTokenUsage(usage: TokenUsage, model: String, operation: String): Unit
}

/**
 * Bridge adapter to convert new [[Tracing]] to legacy [[LegacyTracing]].
 *
 * Wraps a `Tracing` instance and throws `RuntimeException` on errors,
 * providing backward compatibility with code expecting `Unit` returns.
 *
 * @param enhanced The underlying Tracing instance
 */
@deprecated("Use Tracing directly instead", since = "0.5.0")
class LegacyTracingBridge(enhanced: Tracing) extends LegacyTracing {

  def traceEvent(event: String): Unit =
    enhanced.traceEvent(event).left.foreach(error => throw new RuntimeException(s"Tracing error: ${error.message}"))

  def traceAgentState(state: AgentState): Unit =
    enhanced
      .traceAgentState(state)
      .left
      .foreach(error => throw new RuntimeException(s"Tracing error: ${error.message}"))

  def traceToolCall(toolName: String, input: String, output: String): Unit =
    enhanced
      .traceToolCall(toolName, input, output)
      .left
      .foreach(error => throw new RuntimeException(s"Tracing error: ${error.message}"))

  def traceError(error: Throwable): Unit =
    enhanced
      .traceError(error)
      .left
      .foreach(traceError => throw new RuntimeException(s"Tracing error: ${traceError.message}"))

  def traceCompletion(completion: Completion, model: String): Unit =
    enhanced
      .traceCompletion(completion, model)
      .left
      .foreach(error => throw new RuntimeException(s"Tracing error: ${error.message}"))

  def traceTokenUsage(usage: TokenUsage, model: String, operation: String): Unit =
    enhanced
      .traceTokenUsage(usage, model, operation)
      .left
      .foreach(error => throw new RuntimeException(s"Tracing error: ${error.message}"))
}

/**
 * Factory for legacy tracing support.
 */
@deprecated("Use Tracing companion object instead", since = "0.5.0")
object LegacyTracing {

  /** Wrap a Tracing instance in the legacy interface. */
  def fromTracing(tracing: Tracing): LegacyTracing = new LegacyTracingBridge(tracing)

  /** Create legacy tracing from settings. */
  def create(settings: TracingSettings): LegacyTracing = {
    val enhanced = Tracing.create(settings)
    new LegacyTracingBridge(enhanced)
  }
}

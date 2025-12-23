package org.llm4s.trace

import org.llm4s.agent.AgentState
import org.llm4s.llmconnect.model.{ TokenUsage, Completion }

/**
 * No-operation tracing implementation that silently discards all trace events.
 *
 * Use this implementation when tracing is disabled or not needed.
 * All methods complete immediately without any side effects.
 *
 * @example
 * {{{
 * val tracing: Tracing = new NoOpTracing()
 * tracing.traceEvent("ignored") // Does nothing
 * }}}
 *
 * @see [[PrintTracing]] for console output tracing
 * @see [[LangfuseTracing]] for production observability
 */
class NoOpTracing extends Tracing {
  override def traceEvent(event: String): Unit                                            = ()
  override def traceAgentState(state: AgentState): Unit                                   = ()
  override def traceToolCall(toolName: String, input: String, output: String): Unit       = ()
  override def traceError(error: Throwable): Unit                                         = ()
  override def traceCompletion(completion: Completion, model: String): Unit               = ()
  override def traceTokenUsage(usage: TokenUsage, model: String, operation: String): Unit = ()
}

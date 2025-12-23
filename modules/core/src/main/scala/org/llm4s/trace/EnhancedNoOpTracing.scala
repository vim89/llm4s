package org.llm4s.trace

import org.llm4s.agent.AgentState
import org.llm4s.llmconnect.model.{ TokenUsage, Completion }
import org.llm4s.types.Result

/**
 * No-operation implementation of [[EnhancedTracing]] that silently discards all events.
 *
 * All methods return `Right(())` immediately without performing any operations.
 * Use this implementation when tracing is disabled or not needed.
 *
 * @example
 * {{{
 * val tracing: EnhancedTracing = new EnhancedNoOpTracing()
 * tracing.traceEvent(TraceEvent.CustomEvent("test", ujson.Obj())) // Returns Right(())
 * }}}
 *
 * @see [[EnhancedConsoleTracing]] for development/debugging
 * @see [[EnhancedLangfuseTracing]] for production observability
 */
class EnhancedNoOpTracing extends EnhancedTracing {
  def traceEvent(event: TraceEvent): Result[Unit]                                        = Right(())
  def traceAgentState(state: AgentState): Result[Unit]                                   = Right(())
  def traceToolCall(toolName: String, input: String, output: String): Result[Unit]       = Right(())
  def traceError(error: Throwable, context: String): Result[Unit]                        = Right(())
  def traceCompletion(completion: Completion, model: String): Result[Unit]               = Right(())
  def traceTokenUsage(usage: TokenUsage, model: String, operation: String): Result[Unit] = Right(())
}

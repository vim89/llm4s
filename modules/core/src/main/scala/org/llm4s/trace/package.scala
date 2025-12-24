package org.llm4s

/**
 * Tracing package provides observability and debugging capabilities for LLM4S.
 *
 * == Overview ==
 *
 * The trace package offers a unified interface for tracing LLM operations,
 * agent state changes, tool executions, and errors. All implementations
 * return `Result[Unit]` for type-safe error handling.
 *
 * == Implementations ==
 *
 *  - [[trace.ConsoleTracing]] - Prints colored output to console (development)
 *  - [[trace.LangfuseTracing]] - Sends events to Langfuse (production)
 *  - [[trace.NoOpTracing]] - No-op implementation (testing/disabled)
 *
 * == Usage ==
 *
 * {{{
 * import org.llm4s.trace._
 *
 * // Create from settings
 * val tracing = Tracing.create(settings)
 *
 * // Trace events
 * tracing.traceEvent(TraceEvent.AgentInitialized("query", tools))
 * tracing.traceToolCall("calculator", input, output)
 * tracing.traceError(error, "context")
 * }}}
 *
 * == Composition ==
 *
 * Use [[trace.TracingComposer]] to combine, filter, or transform tracers:
 *
 * {{{
 * val combined = TracingComposer.combine(consoleTracer, langfuseTracer)
 * val filtered = TracingComposer.filter(tracer)(_.isError)
 * }}}
 */
package object trace {

  /** @deprecated Use [[Tracing]] instead. This alias exists for backward compatibility. */
  @deprecated("Use Tracing instead", since = "0.5.0")
  type EnhancedTracing = Tracing

  /** @deprecated Use [[Tracing]] companion object instead. */
  @deprecated("Use Tracing companion object instead", since = "0.5.0")
  val EnhancedTracing: Tracing.type = Tracing

  /** @deprecated Use [[ConsoleTracing]] instead. */
  @deprecated("Use ConsoleTracing instead", since = "0.5.0")
  type EnhancedConsoleTracing = ConsoleTracing

  /** @deprecated Use [[NoOpTracing]] instead. */
  @deprecated("Use NoOpTracing instead", since = "0.5.0")
  type EnhancedNoOpTracing = NoOpTracing

  /** @deprecated Use [[LangfuseTracing]] instead. */
  @deprecated("Use LangfuseTracing instead", since = "0.5.0")
  type EnhancedLangfuseTracing = LangfuseTracing
}

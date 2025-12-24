package org.llm4s.trace

import org.llm4s.agent.{ AgentState, AgentStatus }
import org.llm4s.error.SimpleError
import org.llm4s.llmconnect.model._
import org.llm4s.toolapi.ToolRegistry
import org.llm4s.types.Result
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable

/**
 * Tests for LegacyTracingBridge which adapts Tracing to LegacyTracing.
 */
@scala.annotation.nowarn("cat=deprecation")
class TracingBridgeSpec extends AnyFlatSpec with Matchers {

  // ==========================================================================
  // Success Path Tests
  // ==========================================================================

  "LegacyTracingBridge" should "delegate traceEvent to tracing" in {
    val events = mutable.Buffer.empty[String]
    val tracer = new RecordingTracing(events)
    val bridge = LegacyTracing.fromTracing(tracer)

    bridge.traceEvent("test event")

    events should contain("event:test event")
  }

  it should "delegate traceAgentState to tracing" in {
    val events = mutable.Buffer.empty[String]
    val tracer = new RecordingTracing(events)
    val bridge = LegacyTracing.fromTracing(tracer)
    val state  = createTestAgentState()

    bridge.traceAgentState(state)

    events.exists(_.startsWith("agentState:")) shouldBe true
  }

  it should "delegate traceToolCall to tracing" in {
    val events = mutable.Buffer.empty[String]
    val tracer = new RecordingTracing(events)
    val bridge = LegacyTracing.fromTracing(tracer)

    bridge.traceToolCall("calculator", """{"a": 1}""", "2")

    events should contain("toolCall:calculator")
  }

  it should "delegate traceError to tracing" in {
    val events = mutable.Buffer.empty[String]
    val tracer = new RecordingTracing(events)
    val bridge = LegacyTracing.fromTracing(tracer)
    val error  = new RuntimeException("Test error")

    bridge.traceError(error)

    events should contain("error:Test error")
  }

  it should "delegate traceCompletion to tracing" in {
    val events     = mutable.Buffer.empty[String]
    val tracer     = new RecordingTracing(events)
    val bridge     = LegacyTracing.fromTracing(tracer)
    val completion = createTestCompletion()

    bridge.traceCompletion(completion, "gpt-4")

    events should contain("completion:cmpl-123")
  }

  it should "delegate traceTokenUsage to tracing" in {
    val events = mutable.Buffer.empty[String]
    val tracer = new RecordingTracing(events)
    val bridge = LegacyTracing.fromTracing(tracer)
    val usage  = TokenUsage(100, 50, 150)

    bridge.traceTokenUsage(usage, "gpt-4", "completion")

    events should contain("tokenUsage:150")
  }

  // ==========================================================================
  // Error Propagation Tests
  // ==========================================================================

  it should "throw RuntimeException when traceEvent fails" in {
    val tracer = new FailingTracing("Event failed")
    val bridge = LegacyTracing.fromTracing(tracer)

    val exception = intercept[RuntimeException] {
      bridge.traceEvent("will fail")
    }

    exception.getMessage should include("Tracing error")
    exception.getMessage should include("Event failed")
  }

  it should "throw RuntimeException when traceAgentState fails" in {
    val tracer = new FailingTracing("State failed")
    val bridge = LegacyTracing.fromTracing(tracer)
    val state  = createTestAgentState()

    val exception = intercept[RuntimeException] {
      bridge.traceAgentState(state)
    }

    exception.getMessage should include("Tracing error")
    exception.getMessage should include("State failed")
  }

  it should "throw RuntimeException when traceToolCall fails" in {
    val tracer = new FailingTracing("Tool call failed")
    val bridge = LegacyTracing.fromTracing(tracer)

    val exception = intercept[RuntimeException] {
      bridge.traceToolCall("tool", "input", "output")
    }

    exception.getMessage should include("Tracing error")
    exception.getMessage should include("Tool call failed")
  }

  it should "throw RuntimeException when traceError fails" in {
    val tracer = new FailingTracing("Trace error failed")
    val bridge = LegacyTracing.fromTracing(tracer)
    val error  = new RuntimeException("Original error")

    val exception = intercept[RuntimeException] {
      bridge.traceError(error)
    }

    exception.getMessage should include("Tracing error")
    exception.getMessage should include("Trace error failed")
  }

  it should "throw RuntimeException when traceCompletion fails" in {
    val tracer     = new FailingTracing("Completion failed")
    val bridge     = LegacyTracing.fromTracing(tracer)
    val completion = createTestCompletion()

    val exception = intercept[RuntimeException] {
      bridge.traceCompletion(completion, "gpt-4")
    }

    exception.getMessage should include("Tracing error")
    exception.getMessage should include("Completion failed")
  }

  it should "throw RuntimeException when traceTokenUsage fails" in {
    val tracer = new FailingTracing("Token usage failed")
    val bridge = LegacyTracing.fromTracing(tracer)
    val usage  = TokenUsage(100, 50, 150)

    val exception = intercept[RuntimeException] {
      bridge.traceTokenUsage(usage, "gpt-4", "completion")
    }

    exception.getMessage should include("Tracing error")
    exception.getMessage should include("Token usage failed")
  }

  // ==========================================================================
  // Integration with Factory Method
  // ==========================================================================

  it should "work with NoOp tracing" in {
    val tracing = new NoOpTracing()
    val bridge  = LegacyTracing.fromTracing(tracing)

    // All operations should succeed silently
    noException should be thrownBy {
      bridge.traceEvent("test")
      bridge.traceAgentState(createTestAgentState())
      bridge.traceToolCall("tool", "in", "out")
      bridge.traceError(new RuntimeException("error"))
      bridge.traceCompletion(createTestCompletion(), "gpt-4")
      bridge.traceTokenUsage(TokenUsage(1, 1, 2), "gpt-4", "op")
    }
  }

  // ==========================================================================
  // Helper Classes
  // ==========================================================================

  private def createTestAgentState(): AgentState =
    AgentState(
      conversation = Conversation(Seq(UserMessage("Hello"))),
      tools = ToolRegistry.empty,
      status = AgentStatus.InProgress,
      initialQuery = Some("test query"),
      logs = Vector("log1")
    )

  private def createTestCompletion(): Completion =
    Completion(
      id = "cmpl-123",
      created = 1234567890L,
      content = "Hello",
      model = "gpt-4",
      message = AssistantMessage(Some("Hello"), Seq.empty),
      usage = Some(TokenUsage(100, 50, 150))
    )

  /** Test tracer that records operations */
  private class RecordingTracing(events: mutable.Buffer[String]) extends Tracing {
    def traceEvent(event: TraceEvent): Result[Unit] = {
      event match {
        case e: TraceEvent.CustomEvent => events += s"event:${e.name}"
        case _                         => events += s"event:${event.eventType}"
      }
      Right(())
    }

    def traceAgentState(state: AgentState): Result[Unit] = {
      events += s"agentState:${state.status}"
      Right(())
    }

    def traceToolCall(toolName: String, input: String, output: String): Result[Unit] = {
      events += s"toolCall:$toolName"
      Right(())
    }

    def traceError(error: Throwable, context: String): Result[Unit] = {
      events += s"error:${error.getMessage}"
      Right(())
    }

    def traceCompletion(completion: Completion, model: String): Result[Unit] = {
      events += s"completion:${completion.id}"
      Right(())
    }

    def traceTokenUsage(usage: TokenUsage, model: String, operation: String): Result[Unit] = {
      events += s"tokenUsage:${usage.totalTokens}"
      Right(())
    }
  }

  /** Test tracer that always fails with specified message */
  private class FailingTracing(errorMessage: String) extends Tracing {
    private val error = SimpleError(errorMessage)

    def traceEvent(event: TraceEvent): Result[Unit]                                        = Left(error)
    def traceAgentState(state: AgentState): Result[Unit]                                   = Left(error)
    def traceToolCall(toolName: String, input: String, output: String): Result[Unit]       = Left(error)
    def traceError(e: Throwable, context: String): Result[Unit]                            = Left(error)
    def traceCompletion(completion: Completion, model: String): Result[Unit]               = Left(error)
    def traceTokenUsage(usage: TokenUsage, model: String, operation: String): Result[Unit] = Left(error)
  }
}

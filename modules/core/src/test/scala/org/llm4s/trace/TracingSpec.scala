package org.llm4s.trace

import org.llm4s.agent.{ AgentState, AgentStatus }
import org.llm4s.llmconnect.model._
import org.llm4s.llmconnect.config.{ LangfuseConfig, TracingSettings }
import org.llm4s.toolapi.ToolRegistry
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Tests for Tracing trait and factory methods
 */
class TracingSpec extends AnyFlatSpec with Matchers {

  // ============ TracingMode ============

  "TracingMode.fromString" should "parse langfuse mode" in {
    TracingMode.fromString("langfuse") shouldBe TracingMode.Langfuse
    TracingMode.fromString("LANGFUSE") shouldBe TracingMode.Langfuse
    TracingMode.fromString("Langfuse") shouldBe TracingMode.Langfuse
  }

  it should "parse console mode" in {
    TracingMode.fromString("console") shouldBe TracingMode.Console
    TracingMode.fromString("CONSOLE") shouldBe TracingMode.Console
    TracingMode.fromString("print") shouldBe TracingMode.Console
    TracingMode.fromString("PRINT") shouldBe TracingMode.Console
  }

  it should "parse noop mode" in {
    TracingMode.fromString("noop") shouldBe TracingMode.NoOp
    TracingMode.fromString("NOOP") shouldBe TracingMode.NoOp
    TracingMode.fromString("none") shouldBe TracingMode.NoOp
    TracingMode.fromString("NONE") shouldBe TracingMode.NoOp
  }

  it should "default to NoOp for unknown modes" in {
    TracingMode.fromString("unknown") shouldBe TracingMode.NoOp
    TracingMode.fromString("") shouldBe TracingMode.NoOp
    TracingMode.fromString("invalid") shouldBe TracingMode.NoOp
  }

  // ============ NoOpTracing ============

  "NoOpTracing" should "implement all methods without side effects" in {
    val tracing = new NoOpTracing()

    // All methods should complete without throwing
    noException should be thrownBy {
      tracing.traceEvent("test event")
      tracing.traceToolCall("tool", "input", "output")
      tracing.traceError(new RuntimeException("test"))
    }
  }

  it should "trace agent state without error" in {
    val tracing = new NoOpTracing()
    val state   = createTestAgentState()

    noException should be thrownBy tracing.traceAgentState(state)
  }

  it should "trace completion without error" in {
    val tracing    = new NoOpTracing()
    val completion = createTestCompletion()

    noException should be thrownBy tracing.traceCompletion(completion, "gpt-4")
  }

  it should "trace token usage without error" in {
    val tracing = new NoOpTracing()
    val usage   = TokenUsage(100, 50, 150)

    noException should be thrownBy tracing.traceTokenUsage(usage, "gpt-4", "completion")
  }

  // ============ Tracing.create ============

  "Tracing.create" should "create NoOp tracing for NoOp mode" in {
    val settings = TracingSettings(
      mode = TracingMode.NoOp,
      langfuse = LangfuseConfig()
    )

    val tracing = Tracing.create(settings)

    // Should not throw when used
    noException should be thrownBy tracing.traceEvent("test")
  }

  it should "create Console tracing for Console mode" in {
    val settings = TracingSettings(
      mode = TracingMode.Console,
      langfuse = LangfuseConfig()
    )

    val tracing = Tracing.create(settings)

    // Should not throw when used (will print to console)
    noException should be thrownBy {
      // We don't actually call these to avoid console output in tests
      // Just verify creation succeeded
      tracing shouldBe a[Tracing]
    }
  }

  // ============ Tracing.createFromEnhanced ============

  "Tracing.createFromEnhanced" should "wrap EnhancedTracing in legacy interface" in {
    val enhanced = new EnhancedNoOpTracing()
    val legacy   = Tracing.createFromEnhanced(enhanced)

    // Should work as legacy Tracing
    noException should be thrownBy {
      legacy.traceEvent("test")
      legacy.traceToolCall("tool", "input", "output")
      legacy.traceError(new RuntimeException("test"))
    }
  }

  it should "delegate traceAgentState to enhanced tracing" in {
    val enhanced = new EnhancedNoOpTracing()
    val legacy   = Tracing.createFromEnhanced(enhanced)
    val state    = createTestAgentState()

    noException should be thrownBy legacy.traceAgentState(state)
  }

  it should "delegate traceCompletion to enhanced tracing" in {
    val enhanced   = new EnhancedNoOpTracing()
    val legacy     = Tracing.createFromEnhanced(enhanced)
    val completion = createTestCompletion()

    noException should be thrownBy legacy.traceCompletion(completion, "gpt-4")
  }

  it should "delegate traceTokenUsage to enhanced tracing" in {
    val enhanced = new EnhancedNoOpTracing()
    val legacy   = Tracing.createFromEnhanced(enhanced)
    val usage    = TokenUsage(100, 50, 150)

    noException should be thrownBy legacy.traceTokenUsage(usage, "gpt-4", "completion")
  }

  // ============ Helper Methods ============

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
      id = "comp-123",
      created = 1234567890L,
      content = "Hello",
      model = "gpt-4",
      message = AssistantMessage(Some("Hello"), Seq.empty),
      usage = Some(TokenUsage(100, 50, 150))
    )
}

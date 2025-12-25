package org.llm4s.trace

import org.llm4s.config.Llm4sConfig
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TracingSettingsSpec extends AnyWordSpec with Matchers {

  private def isLangfuseConfigured: Boolean =
    Option(System.getenv("LANGFUSE_PUBLIC_KEY")).exists(_.nonEmpty) ||
      Option(System.getenv("TRACING_MODE")).exists(_.equalsIgnoreCase("langfuse")) ||
      Option(System.getProperty("llm4s.tracing.mode")).exists(_.equalsIgnoreCase("langfuse"))

  "Llm4sConfig.tracing + Tracing.create" should {
    "return Console tracing by default" in {
      // Skip test if Langfuse is configured in environment
      if (isLangfuseConfigured) {
        cancel("Test skipped: Langfuse is configured in environment (LANGFUSE_PUBLIC_KEY or TRACING_MODE=langfuse)")
      }

      val res = Llm4sConfig.tracing().map(Tracing.create)
      res.fold(err => fail(err.toString), tracer => tracer shouldBe a[ConsoleTracing])
    }
  }
}

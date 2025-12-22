package org.llm4s.toolapi

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Tests for ToolExecutionStrategy sealed trait and all strategy types
 */
class ToolExecutionStrategySpec extends AnyFlatSpec with Matchers {

  // ============ Sequential Strategy ============

  "ToolExecutionStrategy.Sequential" should "be a valid strategy" in {
    val strategy = ToolExecutionStrategy.Sequential

    strategy shouldBe a[ToolExecutionStrategy]
  }

  // ============ Parallel Strategy ============

  "ToolExecutionStrategy.Parallel" should "be a valid strategy" in {
    val strategy = ToolExecutionStrategy.Parallel

    strategy shouldBe a[ToolExecutionStrategy]
  }

  // ============ ParallelWithLimit Strategy ============

  "ToolExecutionStrategy.ParallelWithLimit" should "accept positive concurrency limit" in {
    val strategy = ToolExecutionStrategy.ParallelWithLimit(5)

    strategy.maxConcurrency shouldBe 5
    strategy shouldBe a[ToolExecutionStrategy]
  }

  it should "accept concurrency limit of 1" in {
    val strategy = ToolExecutionStrategy.ParallelWithLimit(1)

    strategy.maxConcurrency shouldBe 1
  }

  it should "reject zero concurrency limit" in {
    an[IllegalArgumentException] should be thrownBy {
      ToolExecutionStrategy.ParallelWithLimit(0)
    }
  }

  it should "reject negative concurrency limit" in {
    an[IllegalArgumentException] should be thrownBy {
      ToolExecutionStrategy.ParallelWithLimit(-1)
    }
  }

  // ============ Default Strategy ============

  "ToolExecutionStrategy.default" should "be Sequential" in {
    ToolExecutionStrategy.default shouldBe ToolExecutionStrategy.Sequential
  }

  // ============ Pattern Matching ============

  "ToolExecutionStrategy" should "support exhaustive pattern matching" in {
    def describeStrategy(strategy: ToolExecutionStrategy): String = strategy match {
      case ToolExecutionStrategy.Sequential               => "sequential"
      case ToolExecutionStrategy.Parallel                 => "parallel"
      case ToolExecutionStrategy.ParallelWithLimit(limit) => s"parallel-$limit"
    }

    describeStrategy(ToolExecutionStrategy.Sequential) shouldBe "sequential"
    describeStrategy(ToolExecutionStrategy.Parallel) shouldBe "parallel"
    describeStrategy(ToolExecutionStrategy.ParallelWithLimit(3)) shouldBe "parallel-3"
  }
}

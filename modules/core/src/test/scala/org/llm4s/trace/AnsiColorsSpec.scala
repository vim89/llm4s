package org.llm4s.trace

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

/**
 * Tests for AnsiColors utility object.
 */
class AnsiColorsSpec extends AnyFlatSpec with Matchers {

  // ==========================================================================
  // Color Constants Tests
  // ==========================================================================

  "AnsiColors" should "define RESET as escape code" in {
    AnsiColors.RESET shouldBe "\u001b[0m"
  }

  it should "define foreground colors with correct escape codes" in {
    AnsiColors.BLUE shouldBe "\u001b[34m"
    AnsiColors.GREEN shouldBe "\u001b[32m"
    AnsiColors.YELLOW shouldBe "\u001b[33m"
    AnsiColors.RED shouldBe "\u001b[31m"
    AnsiColors.CYAN shouldBe "\u001b[36m"
    AnsiColors.MAGENTA shouldBe "\u001b[35m"
    AnsiColors.GRAY shouldBe "\u001b[90m"
  }

  it should "define BOLD as escape code" in {
    AnsiColors.BOLD shouldBe "\u001b[1m"
  }

  // ==========================================================================
  // Separator Tests
  // ==========================================================================

  "AnsiColors.separator" should "create default separator of 60 equals signs" in {
    val sep = AnsiColors.separator()

    sep shouldBe "=" * 60
    sep.length shouldBe 60
  }

  it should "create separator with custom character" in {
    val sep = AnsiColors.separator('-')

    sep shouldBe "-" * 60
    sep.forall(_ == '-') shouldBe true
  }

  it should "create separator with custom length" in {
    val sep = AnsiColors.separator(length = 20)

    sep.length shouldBe 20
    sep shouldBe "=" * 20
  }

  it should "create separator with custom character and length" in {
    val sep = AnsiColors.separator('*', 10)

    sep shouldBe "**********"
    sep.length shouldBe 10
  }

  it should "handle zero length" in {
    val sep = AnsiColors.separator(length = 0)

    sep shouldBe empty
  }

  // ==========================================================================
  // Colorize Tests
  // ==========================================================================

  "AnsiColors.colorize" should "wrap text with color and reset" in {
    val result = AnsiColors.colorize("Hello", AnsiColors.GREEN)

    result shouldBe s"${AnsiColors.GREEN}Hello${AnsiColors.RESET}"
  }

  it should "handle empty text" in {
    val result = AnsiColors.colorize("", AnsiColors.RED)

    result shouldBe s"${AnsiColors.RED}${AnsiColors.RESET}"
  }

  it should "work with any color" in {
    val colors = Seq(
      AnsiColors.BLUE,
      AnsiColors.GREEN,
      AnsiColors.YELLOW,
      AnsiColors.RED,
      AnsiColors.CYAN,
      AnsiColors.MAGENTA,
      AnsiColors.GRAY
    )

    colors.foreach { color =>
      val result = AnsiColors.colorize("test", color)
      result should startWith(color)
      result should endWith(AnsiColors.RESET)
    }
  }

  // ==========================================================================
  // BoldColor Tests
  // ==========================================================================

  "AnsiColors.boldColor" should "wrap text with bold, color, and reset" in {
    val result = AnsiColors.boldColor("Important", AnsiColors.RED)

    result shouldBe s"${AnsiColors.BOLD}${AnsiColors.RED}Important${AnsiColors.RESET}"
  }

  it should "handle empty text" in {
    val result = AnsiColors.boldColor("", AnsiColors.BLUE)

    result shouldBe s"${AnsiColors.BOLD}${AnsiColors.BLUE}${AnsiColors.RESET}"
  }

  it should "work with any color" in {
    val result = AnsiColors.boldColor("Warning", AnsiColors.YELLOW)

    result should startWith(AnsiColors.BOLD)
    result should include(AnsiColors.YELLOW)
    result should endWith(AnsiColors.RESET)
  }

  // ==========================================================================
  // Integration Tests
  // ==========================================================================

  "AnsiColors" should "produce valid output for console printing" in {
    import AnsiColors._

    // This test verifies the format is correct for terminal output
    val header  = s"$CYAN$BOLD${separator()}$RESET"
    val message = colorize("Success!", GREEN)
    val error   = boldColor("Error occurred", RED)

    header should include(separator())
    message should include("Success!")
    error should include("Error occurred")
  }

  it should "allow composing multiple colors" in {
    import AnsiColors._

    val text = s"${BOLD}${RED}Error:${RESET} ${YELLOW}Warning message${RESET}"

    text should include(BOLD)
    text should include(RED)
    text should include(YELLOW)
    // BOLD, RED, RESET, YELLOW, RESET = 5 escape sequences
    text.count(_ == '\u001b') shouldBe 5
  }
}

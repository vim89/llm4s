package $package;format="package"$

/**
 * This code is part of the Giter8 template llm4s.g8 in llm4s project, which provides a set standard template/archetype
 * for improve developer onboarding, creating new projects using the llm4s library.
 */
class MainSpec extends munit.FunSuite {
  test("basic assertion") {
    assert(1 + 1 == 2)
  }

  test("Test Prompt executor") {
    assert(PromptExecutor.run("Explain what a Monad is in Scala"))
  }
}

package org.llm4s.template

/**
 * This code is part of the Giter8 template llm4s.g8 in llm4s project, which provides a set standard template/archetype
 * for improve developer onboarding, creating new projects using the llm4s library.
 */
class MainSpec extends munit.FunSuite {
  test("basic assertion") {
    assert(1 + 1 == 2)
  }

  test("Test Prompt executor") {
    val prompt = "Explain what a Monad is in Scala"
    val response = PromptExecutor.run(prompt)
    assert(response.nonEmpty, "Response should not be empty")
    assert(response.contains("Incorrect API key provided: your-api*****here. You can find your API key at https://platform.openai.com/account/api-keys."))
  }
}

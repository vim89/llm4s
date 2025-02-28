package org.llm4s.runner

import org.scalatest.funsuite.AnyFunSuite
import org.llm4s.shared._
import upickle.default._

class RunnerMainTest extends AnyFunSuite {

  test("execCommand should return ExecShellResponse for ExecShellCommand") {
    val commandRunner = new CommandRunner()
    val execCommand = ExecShellCommand("123", "ls -la")
    val requestJson = write(execCommand)
    val responseJson = RunnerMain.execCommand(cask.Request(requestJson)).data.toString
    val response = read[ExecShellResponse](responseJson)

    assert(response.commandId == "123")
    assert(response.stdout == "Mocked output")
  }

  test("execCommand should return ListDirectoryResponse for ListDirectoryCommand") {
    val commandRunner = new CommandRunner()
    val listCommand = ListDirectoryCommand("456", "/home/user")
    val requestJson = write(listCommand)
    val responseJson = RunnerMain.execCommand(cask.Request(requestJson)).data.toString
    val response = read[ListDirectoryResponse](responseJson)

    assert(response.commandId == "456")
    assert(response.files == List("file1.txt", "file2.txt"))
  }

  test("execCommand should return ErrorResponse for invalid command") {
    val invalidJson = """{"invalid": "data"}"""
    val responseJson = RunnerMain.execCommand(cask.Request(invalidJson)).data.toString
    val response = read[ErrorResponse](responseJson)

    assert(response.commandId == "unknown")
  }
}

package org.llm4s.shared

import org.scalatest.funsuite.AnyFunSuite
import upickle.default._

class ProtocolCodecTest extends AnyFunSuite {

  test("Encode and decode ExecShellCommand") {
    val command = ExecShellCommand("123", "ls -la")
    val json = write(command)
    val decodedCommand = read[ExecShellCommand](json)

    assert(decodedCommand == command)
  }

  test("Encode and decode ListDirectoryCommand") {
    val command = ListDirectoryCommand("456", "/home/user")
    val json = write(command)
    val decodedCommand = read[ListDirectoryCommand](json)

    assert(decodedCommand == command)
  }
}

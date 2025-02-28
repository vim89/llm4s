package org.llm4s.runner

import org.llm4s.shared._

class CommandRunner {

  def executeCommand(request: Request): Response = {
    try {
      request match {
        case cmd: ExecShellCommand     => handleExecShellCommand(cmd)
        case cmd: ListDirectoryCommand => handleListDirectoryCommand(cmd)
      }
    } catch {
      case e: Exception => ErrorResponse(request.asInstanceOf[{ def commandId: String }].commandId, e.getMessage)
    }
  }

  private def handleExecShellCommand(command: ExecShellCommand): ExecShellResponse = {
    // Mock implementation
    ExecShellResponse(command.commandId, command.shellCommand, "Mocked output", 0)
  }

  private def handleListDirectoryCommand(command: ListDirectoryCommand): ListDirectoryResponse = {
    // Mock implementation
    ListDirectoryResponse(command.commandId, List("file1.txt", "file2.txt"))
  }
}

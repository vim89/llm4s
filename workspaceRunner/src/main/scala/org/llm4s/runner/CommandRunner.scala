package org.llm4s.runner

import org.llm4s.shared._

class CommandRunner {

  def executeCommand(request: WorkspaceCommandRequest): WorkspaceCommandResponse = {
    try {
      request match {
        case cmd: ExecShellCommand     => handleExecShellCommand(cmd)
        case cmd: ListDirectoryCommand => handleListDirectoryCommand(cmd)
      }
    } catch {
      case e: Exception =>
        ErrorResponse(request.commandId, e.getMessage)
    }
  }

  private def handleExecShellCommand(command: ExecShellCommand): ExecShellResponse = {
    import scala.sys.process._
    import scala.util.{Try, Success, Failure}

    val commandId    = command.commandId
    val shellCommand = command.shellCommand

    Try {
      val output = shellCommand.!!
      ExecShellResponse(commandId, shellCommand, output, 0)
    } match {
      case Success(response)  => response
      case Failure(exception) => ExecShellResponse(commandId, shellCommand, exception.getMessage, 1)
    }
  }

  private def handleListDirectoryCommand(command: ListDirectoryCommand): WorkspaceCommandResponse = {
    import scala.sys.process._
    import scala.util.{Try, Success, Failure}

    val directory = command.path
    val commandId = command.commandId

    Try {
      val output = Seq("ls", "-1", directory).!!
      val files  = output.split("\n").toList
      ListDirectoryResponse(commandId, files)
    } match {
      case Success(response)  => response
      case Failure(exception) => ErrorResponse(commandId, exception.getMessage)
    }
  }
}

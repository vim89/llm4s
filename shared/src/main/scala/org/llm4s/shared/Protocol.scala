package org.llm4s.shared

import upickle.default.{ReadWriter, macroRW}

sealed trait WorkspaceCommandRequest {
  def commandId: String
}

object WorkspaceCommandRequest {
  implicit val rw: ReadWriter[WorkspaceCommandRequest] = ReadWriter.merge(
    macroRW[ListDirectoryCommand],
    macroRW[ExecShellCommand]
  )
}

sealed trait WorkspaceCommandResponse

object WorkspaceCommandResponse {
  implicit val rw: ReadWriter[WorkspaceCommandResponse] = ReadWriter.merge(
    macroRW[ListDirectoryResponse],
    macroRW[ErrorResponse],
    macroRW[ExecShellResponse]
  )
}

case class ExecShellResponse(commandId: String, stdin: String, stdout: String, returnCode: Int)
    extends WorkspaceCommandResponse

object ExecShellResponse {
  implicit val rw: ReadWriter[ExecShellResponse] = macroRW
}

case class ExecShellCommand(commandId: String, shellCommand: String) extends WorkspaceCommandRequest

object ExecShellCommand {
  implicit val rw: ReadWriter[ExecShellCommand] = macroRW
}

case class ListDirectoryCommand(commandId: String, path: String) extends WorkspaceCommandRequest

object ListDirectoryCommand {
  implicit val rw: ReadWriter[ListDirectoryCommand] = macroRW
}

case class ListDirectoryResponse(commandId: String, files: List[String]) extends WorkspaceCommandResponse

object ListDirectoryResponse {
  implicit val rw: ReadWriter[ListDirectoryResponse] = macroRW
}

case class ErrorResponse(commandId: String, error: String) extends WorkspaceCommandResponse

object ErrorResponse {
  implicit val rw: ReadWriter[ErrorResponse] = macroRW
}

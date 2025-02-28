package org.llm4s.shared

import upickle.default.{ReadWriter, macroRW}

sealed trait Request

object Request {
  implicit val rw: ReadWriter[Request] = ReadWriter.merge(
    macroRW[ListDirectoryCommand]
  )
}

case class ListDirectoryCommand(commandId: String, path: String) extends Request

object ListDirectoryCommand {
  implicit val rw: ReadWriter[ListDirectoryCommand] = macroRW
}

sealed trait Response
case class ListDirectoryResponse(commandId: String, files: List[String]) extends Response

object ListDirectoryResponse {
  implicit val rw: ReadWriter[ListDirectoryResponse] = macroRW
}

case class ErrorResponse(commandId: String, errorMessage: String) extends Response

object ErrorResponse {
  implicit val rw: ReadWriter[ErrorResponse] = macroRW
}

package org.llm4s.shared

import upickle.default._

/**
 * JSON codec for workspace agent protocol messages.
 *
 * Provides serialization and deserialization of commands and responses
 * using uPickle's JSON support. All protocol types are serialized
 * polymorphically based on their runtime type.
 *
 * == Usage Example ==
 * {{{
 * val command: WorkspaceAgentCommand = ReadFileCommand("cmd-1", "/path/to/file.txt", None, None)
 * val json = ProtocolCodec.encodeAgentCommand(command)
 * val decoded = ProtocolCodec.decodeAgentCommand(json)
 * }}}
 *
 * @see [[WorkspaceAgentCommand]] for available commands
 * @see [[WorkspaceAgentResponse]] for response types
 */
object ProtocolCodec {

  /**
   * Decodes a JSON string to a WorkspaceAgentCommand.
   *
   * @param json JSON string representation of a command
   * @return decoded command
   * @throws upickle.core.AbortException if JSON is invalid or doesn't match expected schema
   */
  def decodeAgentCommand(json: String): WorkspaceAgentCommand =
    read[WorkspaceAgentCommand](json)

  /**
   * Encodes a WorkspaceAgentCommand to JSON string.
   *
   * @param command command to encode
   * @return JSON string representation
   */
  def encodeAgentCommand(command: WorkspaceAgentCommand): String =
    write(command)

  /**
   * Decodes a JSON string to a WorkspaceAgentResponse.
   *
   * @param json JSON string representation of a response
   * @return decoded response
   * @throws upickle.core.AbortException if JSON is invalid or doesn't match expected schema
   */
  def decodeAgentResponse(json: String): WorkspaceAgentResponse =
    read[WorkspaceAgentResponse](json)

  /**
   * Encodes a WorkspaceAgentResponse to JSON string.
   *
   * @param response response to encode
   * @return JSON string representation
   */
  def encodeAgentResponse(response: WorkspaceAgentResponse): String =
    write(response)
}

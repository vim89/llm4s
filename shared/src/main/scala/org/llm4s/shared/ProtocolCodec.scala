package org.llm4s.shared

import upickle.default._

object ProtocolCodec {
  def decodeRequest(json: String): WorkspaceCommandRequest = {
    read[WorkspaceCommandRequest](json)
  }
  def encodeRequest(request: WorkspaceCommandRequest): String = {
    write(request)
  }
}

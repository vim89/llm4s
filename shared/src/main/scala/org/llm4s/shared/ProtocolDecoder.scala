package org.llm4s.shared

import upickle.default._

object ProtocolCodec {
  def decodeRequest(json: String): Request = {
    read[Request](json)
  }
  def encodeRequest(request: Request): String = {
    write(request)
  }
}

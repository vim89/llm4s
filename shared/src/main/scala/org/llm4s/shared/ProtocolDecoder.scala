package org.llm4s.shared

import upickle.default._

object ProtocolDecoder {
  def decodeRequest(json: String): Request = {
    read[Request](json)
  }
}

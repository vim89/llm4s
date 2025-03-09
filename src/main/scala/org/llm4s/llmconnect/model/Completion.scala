package org.llm4s.llmconnect.model

case class Completion(
  id: String,
  created: Long,
  message: AssistantMessage,
  usage: Option[TokenUsage] = None
)

case class TokenUsage(
  promptTokens: Int,
  completionTokens: Int,
  totalTokens: Int
)

case class StreamedChunk(
  id: String,
  content: Option[String],
  toolCall: Option[ToolCall] = None,
  finishReason: Option[String] = None
)

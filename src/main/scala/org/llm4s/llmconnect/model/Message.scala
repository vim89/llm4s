package org.llm4s.llmconnect.model

sealed trait Message {
  def role: String
  def content: String
}

case class UserMessage(content: String) extends Message {
  val role = "user"
}

case class SystemMessage(content: String) extends Message {
  val role = "system"
}

case class AssistantMessage(
  content: String,
  toolCalls: Seq[ToolCall] = Seq.empty
) extends Message {
  val role = "assistant"
}

case class ToolMessage(
  toolCallId: String,
  content: String
) extends Message {
  val role = "tool"
}

case class ToolCall(
  id: String,
  name: String,
  arguments: ujson.Value
)

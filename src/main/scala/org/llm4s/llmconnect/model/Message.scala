package org.llm4s.llmconnect.model

sealed trait Message {
  def role: String
  def content: String

  override def toString: String = s"${role}: ${content}"
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

  override def toString: String = {
    val toolCallsStr = if (toolCalls.nonEmpty) {
      s"\nTool Calls: ${toolCalls.map(tc => s"[${tc.id}: ${tc.name}(${tc.arguments})]").mkString(", ")}"
    } else " - no tool calls"

    s"${role}: ${content}${toolCallsStr}"
  }
}

case class ToolMessage(
  toolCallId: String,
  content: String
) extends Message {
  val role = "tool"

  override def toString: String = s"${role}(${toolCallId}): ${content}"
}

case class ToolCall(
  id: String,
  name: String,
  arguments: ujson.Value
) {
  override def toString: String = s"ToolCall($id, $name, $arguments)"
}

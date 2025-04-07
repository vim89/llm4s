package org.llm4s.llmconnect.model

case class Conversation(messages: Seq[Message]) {
  // Add a message and return a new Conversation
  def addMessage(message: Message): Conversation =
    Conversation(messages :+ message)

  // Add multiple messages and return a new Conversation
  def addMessages(newMessages: Seq[Message]): Conversation =
    Conversation(messages ++ newMessages)
}

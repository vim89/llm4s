package org.llm4s.llmconnect.model

import org.llm4s.toolapi.ToolFunction

case class CompletionOptions(
  temperature: Double = 0.7,
  topP: Double = 1.0,
  maxTokens: Option[Int] = None,
  presencePenalty: Double = 0.0,
  frequencyPenalty: Double = 0.0,
  tools: Seq[ToolFunction[_, _]] = Seq.empty
)

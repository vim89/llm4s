package org.llm4s.samples.basic

import org.llm4s.agent.{ AgentState, AgentStatus }
import org.llm4s.llmconnect.LLMConnect
import org.llm4s.llmconnect.model._
import org.llm4s.toolapi.ToolRegistry
import org.llm4s.trace.Tracing
import org.llm4s.config.ConfigReader

object BasicLLMCallingWithTrace {
  def main(args: Array[String]): Unit = {
    val result = for {
      tracingSettings <- ConfigReader.TracingConf()
      client          <- LLMConnect.fromEnv()
      _ = {
        val tracer = Tracing.create(tracingSettings)

        // Create a conversation with messages
        val conversation = Conversation(
          Seq(
            SystemMessage("You are a helpful assistant. You will talk like a pirate."),
            UserMessage("Please write a scala function to add two integers"),
            AssistantMessage("Of course, me hearty! What can I do for ye?"),
            UserMessage("What's the best way to train a parrot?")
          )
        )

        // Trace the start of the agent run
        tracer.traceEvent("Starting LLM conversation")

        // Complete the conversation
        client.complete(conversation) match {
          case Right(completion) =>
            println(s"Model ID=${completion.id} is created at ${completion.created}")
            println(s"Chat Role: ${completion.message.role}")
            println("Message:")
            println(completion.message.content)

            // Trace the completion with token usage
            tracer.traceCompletion(completion, completion.model)

            // Trace the agent state after completion
            val agentState = AgentState(
              conversation = conversation.copy(messages = conversation.messages :+ completion.message),
              tools = new ToolRegistry(Seq()),
              userQuery = conversation.messages.collectFirst { case UserMessage(content) => content }.getOrElse(""),
              status = AgentStatus.Complete,
              logs = Seq(s"Model ID=${completion.id}")
            )
            tracer.traceAgentState(agentState)

            // Trace tool calls if present
            completion.message.toolCalls.foreach { tc =>
              tracer.traceToolCall(tc.name, tc.arguments.render(), "(tool output not available in this example)")
            }

          case Left(error) =>
            tracer.traceError(new RuntimeException(error.formatted))
            println(s"Error: ${error.formatted}")
        }
        tracer.traceEvent("LLM conversation finished")
      }
    } yield ()
    result.fold(err => println(s"Error: ${err.formatted}"), identity)
  }
}

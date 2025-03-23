package org.llm4s.agent

import org.llm4s.llmconnect.LLMClient
import org.llm4s.llmconnect.model._
import org.llm4s.toolapi._

import scala.annotation.tailrec
import scala.util.{ Failure, Success, Try }

/**
 * Basic Agent implementation.
 */
class Agent(client: LLMClient) {

  /**
   * Initializes a new agent state with the given query
   */
  def initialize(query: String, tools: ToolRegistry): AgentState = {
    val initialMessages = Seq(
      SystemMessage(
        """You are a helpful assistant with access to tools. 
        |Follow these steps:
        |1. Analyze the user's question and determine which tools you need to use
        |2. Use the necessary tools to find the information needed
        |3. When you have enough information, provide a helpful final answer
        |4. Think step by step and be thorough""".stripMargin
      ),
      UserMessage(query)
    )

    AgentState(
      conversation = Conversation(initialMessages),
      tools = tools,
      userQuery = query
    )
  }

  /**
   * Runs a single step of the agent's reasoning process
   */
  def runStep(state: AgentState): Either[LLMError, AgentState] =
    state.status match {
      case AgentStatus.InProgress =>
        // Get tools from registry and create completion options
        val options = CompletionOptions(tools = state.tools.tools)

        println(f"Running completion step with ${state.tools.tools.map(_.name).mkString(",")}...")
        // Request next step from LLM
        client.complete(state.conversation, options) match {
          case Right(completion) =>
            val logMessage = completion.message.toolCalls match {
              case Seq() => s"[assistant] text: ${completion.message.content}"
              case toolCalls =>
                val toolNames = toolCalls.map(_.name).mkString(", ")
                s"[assistant] tools: ${toolCalls.size} tool calls requested (${toolNames})"
            }

            val updatedState = state
              .log(logMessage)
              .addMessage(completion.message)

            completion.message.toolCalls match {
              case Seq() =>
                // No tool calls - agent is ready to answer
                Right(updatedState.withStatus(AgentStatus.Complete))

              case toolCalls =>
                // Don't process tools yet, just mark as waiting
                println("Tool calls identified, setting state to waiting for tools...")
                Right(updatedState.withStatus(AgentStatus.WaitingForTools))
            }

          case Left(error) =>
            Left(error)
        }

      case AgentStatus.WaitingForTools =>
        // Get the latest assistant message with tool calls
        val assistantMessageOpt = state.conversation.messages.reverse
          .collectFirst { case msg: AssistantMessage if msg.toolCalls.nonEmpty => msg }

        assistantMessageOpt match {
          case Some(assistantMessage) =>
            // Log summary of tools to be processed
            val toolNames    = assistantMessage.toolCalls.map(_.name).mkString(", ")
            val logMessage   = s"[tools] executing ${assistantMessage.toolCalls.size} tools (${toolNames})"
            val stateWithLog = state.log(logMessage)

            // Process the tool calls
            Try {
              println("Processing tool calls...")
              processToolCalls(stateWithLog, assistantMessage.toolCalls)
            } match {
              case Success(newState) =>
                println("Tool processing successful - continuing")
                Right(newState.withStatus(AgentStatus.InProgress))
              case Failure(error) =>
                Right(stateWithLog.withStatus(AgentStatus.Failed(error.getMessage)))
            }

          case None =>
            // Shouldn't happen, but handle gracefully
            Right(state.withStatus(AgentStatus.Failed("No tool calls found in conversation")))
        }

      case _ =>
        // If the agent is already complete or failed, don't do anything
        Right(state)
    }

  /**
   * Process tool calls and add the results to the conversation
   */
  private def processToolCalls(state: AgentState, toolCalls: Seq[ToolCall]): AgentState = {
    val toolRegistry = state.tools

    // Process each tool call and create tool messages
    val toolMessages = toolCalls.map { toolCall =>
      val request = ToolCallRequest(toolCall.name, toolCall.arguments)
      val result  = toolRegistry.execute(request)

      val resultContent = result match {
        case Right(json) => json.render()
        case Left(error) => s"""{ "isError": true, "message": "$error" }"""
      }

      state.log(s"[tool] ${toolCall.name}: $resultContent")
      ToolMessage(toolCall.id, resultContent)
    }

    // Add the tool messages to the conversation
    state.addMessages(toolMessages)
  }

  /**
   * Runs the agent until completion, failure, or step limit is reached
   */
  def run(query: String, tools: ToolRegistry, maxSteps: Option[Int] = None): Either[LLMError, AgentState] = {
    val initialState = initialize(query, tools)

    @tailrec
    def runUntilCompletion(state: AgentState, stepsRemaining: Option[Int] = maxSteps): Either[LLMError, AgentState] =
      (state.status, stepsRemaining) match {
        // Check for step limit before executing either type of step
        case (s, Some(0)) if s == AgentStatus.InProgress || s == AgentStatus.WaitingForTools =>
          // Step limit reached
          Right(state.log("[system] Step limit reached").withStatus(AgentStatus.Failed("Maximum step limit reached")))

        // Continue if we're in progress or waiting for tools
        case (s, _) if s == AgentStatus.InProgress || s == AgentStatus.WaitingForTools =>
          runStep(state) match {
            case Right(newState) =>
              // Only decrement steps when going from InProgress to WaitingForTools or back to InProgress
              // This means one "logical step" includes both the LLM call and tool execution
              val shouldDecrementStep =
                (state.status == AgentStatus.InProgress && newState.status == AgentStatus.WaitingForTools) ||
                  (state.status == AgentStatus.WaitingForTools && newState.status == AgentStatus.InProgress)

              val nextSteps = if (shouldDecrementStep) stepsRemaining.map(_ - 1) else stepsRemaining
              runUntilCompletion(newState, nextSteps)

            case Left(error) =>
              Left(error)
          }

        case (_, _) =>
          Right(state) // Complete or Failed
      }

    runUntilCompletion(initialState)
  }
}

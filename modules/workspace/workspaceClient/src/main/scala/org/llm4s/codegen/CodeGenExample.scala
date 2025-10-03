package org.llm4s.codegen

import org.llm4s.agent.AgentState
import org.llm4s.error.SimpleError
import org.llm4s.llmconnect.LLMConnect
import org.llm4s.llmconnect.model.MessageRole
import org.slf4j.LoggerFactory

import scala.util.Using

/**
 * Example demonstrating how to use the CodeWorker to perform code tasks.
 */
object CodeGenExample {
  private val logger = LoggerFactory.getLogger(getClass)

  def main(args: Array[String]): Unit = {
    // TODO need to get workspace and traceLogPath dir from config/env vars

    // Trace log path comes from settings below

    val task =
      """Create a simple sbt project containing a hello world example that prints the current date and time.
        |Use 'sbt compile' and 'sbt run' to test the generated code.
        |You can assume you have sbt and java already installed.
        |Run the program and show the result.""".stripMargin

    // TODO refactor and get it from a config or pass it as command arg parameter
    val result = for {
      ws <- WorkspaceSettings.load()
      _ = logger.info(s"Using workspace directory: ${ws.workspaceDir}")
      _ = logger.info(s"Trace log will be written to: ${ws.traceLogPath}")
      client <- LLMConnect.fromEnv()
      finalState <- Using.resource(new CodeWorker(ws.workspaceDir, ws.imageName, ws.hostPort, client)) { codeWorker =>
        for {
          _          <- Either.cond(codeWorker.initialize(), (), SimpleError("Failed to initialize CodeWorker"))
          finalState <- codeWorker.executeTask(task, Some(20), Some(ws.traceLogPath))
          _ = logFinalResponse(finalState, ws.traceLogPath)
        } yield finalState
      }
    } yield finalState

    result match {
      case Right(finalState) =>
        logger.info(s"Workflow completed successfully. Final status: ${finalState.status}")
      case Left(err) =>
        logger.error(s"Workflow failed: ${err.message}")
    }
  }

  private def logFinalResponse(finalState: AgentState, traceLogPath: String): Unit = {
    finalState.conversation.messages.lastOption match {
      case Some(msg) if msg.role == MessageRole.Assistant =>
        logger.info(s"Final agent response: ${msg.content}")
      case _ =>
        logger.warn("No final assistant message found")
    }

    if (finalState.logs.nonEmpty) {
      logger.info(s"Execution logs (see also $traceLogPath):")
      finalState.logs.foreach(logger.info)
    }
  }
}

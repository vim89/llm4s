package org.llm4s.samples.workspace

import org.llm4s.workspace.ContainerisedWorkspace
import org.slf4j.LoggerFactory

/** Demonstrates the basic usage of the Workspace class
  */
object ContainerisedWorkspaceDemo extends App {
  private val logger = LoggerFactory.getLogger(getClass)

  // Sample workspace directory on the host machine
  val workspaceDir = System.getProperty("user.home") + "/workspace-demo"
  logger.info(s"Using workspace directory: $workspaceDir")

  val workspace = new ContainerisedWorkspace(workspaceDir)

  try {
    // Start the workspace container
    if (workspace.startContainer()) {
      logger.info("Container started successfully")

      // List the workspace directory
      val dirContents = workspace.listDirectory("/workspace")
      logger.info(s"Initial directory contents: ${dirContents.files.mkString(", ")}")

      // Execute a simple command
      val echoResult = workspace.execShellCommand("echo 'Hello from workspace'")
      logger.info(s"Echo command result (exit code ${echoResult.returnCode}): ${echoResult.stdout}")

      // Create a new file
      val touchResult = workspace.execShellCommand("touch /workspace/test_file.txt")
      logger.info(s"Touch command exit code: ${touchResult.returnCode}")

      // List the directory again to see the new file
      val updatedContents = workspace.listDirectory("/workspace")
      logger.info(s"Updated directory contents: ${updatedContents.files.mkString(", ")}")

      // Write some content to the file
      val writeResult = workspace.execShellCommand("echo 'This is a test file' > /workspace/test_file.txt")
      logger.info(s"Write to file exit code: ${writeResult.returnCode}")

      // Read the file content
      val catResult = workspace.execShellCommand("cat /workspace/test_file.txt")
      logger.info(s"File content: ${catResult.stdout}")
    } else {
      logger.error("Failed to start the workspace container")
    }
  } catch {
    case e: Exception =>
      logger.error(s"Error during workspace demo: ${e.getMessage}", e)
  } finally {
    // Always clean up the container
    if (workspace.stopContainer()) {
      logger.info("Container stopped successfully")
    } else {
      logger.error("Failed to stop the container")
    }
  }
}

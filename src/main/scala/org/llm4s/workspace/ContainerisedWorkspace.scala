package org.llm4s.workspace

import org.llm4s.shared._
import org.slf4j.LoggerFactory
import upickle.default._

import java.io.{ BufferedReader, File, InputStreamReader }
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.{ Executors, ScheduledExecutorService, TimeUnit }
import scala.util.{ Failure, Success, Try }

/**
 * Mount the workspace `workspaceDir` as read/writable inside a container as /workspace and provide
 * the command interface to that workspace.
 *
 * Mounting the workspace inside a container provides a secure and isolated environment for running
 * arbitrary commands.   The worst action a command can take is to corrupt the workspace directory.
 */
class ContainerisedWorkspace(val workspaceDir: String) extends WorkspaceAgentInterface {
  private val logger        = LoggerFactory.getLogger(getClass)
  private val containerName = s"workspace-runner-${java.util.UUID.randomUUID().toString}"
  private val port          = 8080
  private val baseUrl       = s"http://localhost:$port"

  // Constants
  private val HeartbeatIntervalSeconds = 5
  private val HeartbeatTimeoutMs       = 15000
  private val MaxStartupAttempts       = 10

  // State tracking
  private val containerRunning                            = new AtomicBoolean(false)
  private val heartbeatExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()

  /**
   * Starts the workspace runner docker container
   * @return true if the container was started successfully, false otherwise
   */
  def startContainer(): Boolean = {
    logger.info(s"Starting workspace runner container: $containerName")

    // Ensure the workspace directory exists
    val dir = new File(workspaceDir)
    if (!dir.exists()) {
      dir.mkdirs()
    }

    // Run the docker container with the workspace directory mounted
    val dockerProcess = Try {
      val pb = new java.lang.ProcessBuilder(
        "docker",
        "run",
        "-d",
        "--name",
        containerName,
        "-p",
        s"$port:8080",
        "-v",
        s"$workspaceDir:/workspace",
        "docker.io/library/workspace-runner:0.1.0-SNAPSHOT"
      )
      val process  = pb.start()
      val exitCode = process.waitFor()

      // Capture and log stdout
      val stdoutReader = new BufferedReader(new InputStreamReader(process.getInputStream))
      val stdout       = Iterator.continually(stdoutReader.readLine()).takeWhile(_ != null).mkString("\n")
      if (stdout.nonEmpty) logger.info(s"Docker stdout: $stdout")

      // Capture and log stderr
      val stderrReader = new BufferedReader(new InputStreamReader(process.getErrorStream))
      val stderr       = Iterator.continually(stderrReader.readLine()).takeWhile(_ != null).mkString("\n")
      if (stderr.nonEmpty) logger.warn(s"Docker stderr: $stderr")

      (exitCode, stdout, stderr)
    }

    dockerProcess match {
      case Success((0, containerId, _)) =>
        logger.info(s"Container $containerName started successfully with ID: ${containerId.trim}")

        // Wait until the container is responsive
        if (waitForContainerStartup()) {
          containerRunning.set(true)
          startHeartbeatTask()
          true
        } else {
          logger.error("Container started but service is not responding")
          stopContainer()
          false
        }

      case Success((exitCode, _, stderr)) =>
        logger.error(s"Failed to start container, exit code: $exitCode, stderr: $stderr")
        false

      case Failure(ex) =>
        logger.error(s"Exception starting container: ${ex.getMessage}", ex)
        false
    }
  }

  private def waitForContainerStartup(): Boolean = {
    logger.info("Waiting for container service to be ready...")

    var attempts = 0
    while (attempts < MaxStartupAttempts)
      Try {
        requests.get(s"$baseUrl/heartbeat", readTimeout = 1000, connectTimeout = 1000)
      } match {
        case Success(response) if response.statusCode == 200 =>
          logger.info("Service is ready and responding to heartbeats")
          return true
        case _ =>
          attempts += 1
          logger.debug(s"Waiting for service to be ready (attempt $attempts/$MaxStartupAttempts)")
          Thread.sleep(1000)
      }

    logger.error(s"Service failed to respond within $MaxStartupAttempts attempts")
    false
  }

  private def startHeartbeatTask(): Unit = {
    logger.info("Starting heartbeat task")

    heartbeatExecutor.scheduleAtFixedRate(
      () =>
        if (containerRunning.get()) {
          try {
            val response = requests.get(s"$baseUrl/heartbeat", readTimeout = 2000, connectTimeout = 2000)
            if (response.statusCode != 200) {
              logger.warn(s"Heartbeat failed with status ${response.statusCode}")
              handleContainerDown()
            }

          } catch {
            case e: Exception =>
              logger.warn(s"Failed to send heartbeat: ${e.getMessage}")
              handleContainerDown()
          }
        },
      0,
      HeartbeatIntervalSeconds,
      TimeUnit.SECONDS
    )
  }

  private def handleContainerDown(): Unit =
    if (containerRunning.compareAndSet(true, false)) {
      logger.error("Container appears to be down - no longer receiving heartbeats")
      // We don't stop the heartbeat executor in case the container comes back up
    }

  def stopContainer(): Boolean = {
    logger.info(s"Stopping workspace runner container: $containerName")
    containerRunning.set(false)

    // Shutdown heartbeat task
    heartbeatExecutor.shutdown()
    try
      if (!heartbeatExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
        heartbeatExecutor.shutdownNow()
      }
    catch {
      case _: InterruptedException => heartbeatExecutor.shutdownNow()
    }

    // Execute stop and remove as separate commands
    val stopResult = Try {
      val process  = Runtime.getRuntime.exec(Array("docker", "stop", containerName))
      val exitCode = process.waitFor()
      val stderr   = scala.io.Source.fromInputStream(process.getErrorStream).mkString.trim
      (exitCode, stderr)
    }

    val rmResult = stopResult match {
      case Success((0, _)) =>
        Try {
          val process  = Runtime.getRuntime.exec(Array("docker", "rm", containerName))
          val exitCode = process.waitFor()
          val stderr   = scala.io.Source.fromInputStream(process.getErrorStream).mkString.trim
          (exitCode, stderr)
        }
      case Success((_, stderr)) =>
        logger.error(s"Failed to stop container: $stderr")
        Failure(new RuntimeException(s"Container stop failed: $stderr"))
      case Failure(ex) =>
        Failure(ex)
    }

    (stopResult, rmResult) match {
      case (Success((0, _)), Success((0, _))) =>
        logger.info(s"Container $containerName stopped and removed successfully")
        true
      case (Success((0, _)), Success((_, stderr))) =>
        logger.error(s"Failed to remove container: $stderr")
        false
      case (Success((0, _)), Failure(ex)) =>
        logger.error(s"Exception removing container: ${ex.getMessage}", ex)
        false
      case _ =>
        logger.error(s"Failed to stop and remove container")
        false
    }
  }

  // Create a remote interface implementation that uses sendCommandRequest
  private val remoteInterface = WorkspaceAgentInterfaceRemote(sendCommandRequest)

  /**
   * Sends a command request to the workspace runner
   * @param request The request to send
   * @return The response from the workspace runner
   */
  private def sendCommandRequest(request: WorkspaceAgentCommand): WorkspaceAgentResponse = {
    if (!containerRunning.get()) {
      throw new RuntimeException("Container is not running - cannot send command")
    }

    try {
      val endpoint    = s"$baseUrl/agent"
      val requestBody = write(request)

      logger.debug(s"Sending request to $endpoint: $requestBody")

      val response = requests.post(
        endpoint,
        data = requestBody,
        headers = Map("Content-Type" -> "application/json"),
        readTimeout = 10000,
        connectTimeout = 3000
      )

      if (response.statusCode == 200) {
        read[WorkspaceAgentResponse](response.text())
      } else {
        logger.error(s"Request failed with status code: ${response.statusCode}, body: ${response.text()}")
        WorkspaceAgentErrorResponse(request.commandId, "WORKSPACE_ERROR", s"HTTP error: ${response.statusCode}")
      }
    } catch {
      case ex: Exception =>
        // If we get an exception, the container might be down
        handleContainerDown()
        logger.error(s"Exception sending request: ${ex.getMessage}", ex)
        WorkspaceAgentErrorResponse(request.commandId, "WORKSPACE_ERROR", s"Unknown error : ${ex.getMessage}")
    }
  }

  // WorkspaceAgentInterface implementation - delegate to remoteInterface
  override def exploreFiles(
    path: String,
    recursive: Option[Boolean] = None,
    excludePatterns: Option[List[String]] = None,
    maxDepth: Option[Int] = None,
    returnMetadata: Option[Boolean] = None
  ): ExploreFilesResponse =
    remoteInterface.exploreFiles(path, recursive, excludePatterns, maxDepth, returnMetadata)

  override def readFile(
    path: String,
    startLine: Option[Int] = None,
    endLine: Option[Int] = None
  ): ReadFileResponse =
    remoteInterface.readFile(path, startLine, endLine)

  override def writeFile(
    path: String,
    content: String,
    mode: Option[String] = None,
    createDirectories: Option[Boolean] = None
  ): WriteFileResponse =
    remoteInterface.writeFile(path, content, mode, createDirectories)

  override def modifyFile(
    path: String,
    operations: List[FileOperation]
  ): ModifyFileResponse =
    remoteInterface.modifyFile(path, operations)

  override def searchFiles(
    paths: List[String],
    query: String,
    searchType: String,
    recursive: Option[Boolean] = None,
    excludePatterns: Option[List[String]] = None,
    contextLines: Option[Int] = None
  ): SearchFilesResponse =
    remoteInterface.searchFiles(paths, query, searchType, recursive, excludePatterns, contextLines)

  override def executeCommand(
    command: String,
    workingDirectory: Option[String] = None,
    timeout: Option[Int] = None,
    environment: Option[Map[String, String]] = None
  ): ExecuteCommandResponse =
    remoteInterface.executeCommand(command, workingDirectory, timeout, environment)

  override def getWorkspaceInfo(): GetWorkspaceInfoResponse =
    remoteInterface.getWorkspaceInfo()
}

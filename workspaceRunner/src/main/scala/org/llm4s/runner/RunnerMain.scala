package org.llm4s.runner

import cask.model.Response
import org.slf4j.LoggerFactory
import org.llm4s.shared._
import upickle.default._
import java.util.concurrent.{Executors, ScheduledExecutorService, TimeUnit}
import java.util.concurrent.atomic.AtomicLong

object RunnerMain extends cask.MainRoutes {

  private val logger                                     = LoggerFactory.getLogger(getClass)
  private val watchdogExecutor: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor()
  private val lastHeartbeatTime                          = new AtomicLong(System.currentTimeMillis())
  private val heartbeatTimeoutMs                         = 10000L // 10 seconds in milliseconds

  val commandRunner = new CommandRunner()

  // default is localhost - macs bind localhost to ipv6 by default
  // so it doesn't work correctly.  This is a workaround
  // 0.0.0.0 binds to all interfaces - find for us as we are going
  // to run this in a docker container.
  // On mac you can bind to 'localhost' or 127.0.0.1 which changes
  // the behaviour but not sure what impact that has on other platforms
  override def host: String = "0.0.0.0"

  @cask.get("/")
  def root(): String = {
    "LLM4S Runner service - please use the rest endpoint"
  }

  @cask.post("/execCommand")
  def execCommand(request: cask.Request): Response[String] = {
    // Refresh heartbeat on any command execution
    updateHeartbeat()
    try {
      val requestBody = request.text()
      val requestObj  = read[WorkspaceCommandRequest](requestBody)
      val responseObj = commandRunner.executeCommand(requestObj)
      cask.Response(write(responseObj), 200)
    } catch {
      case e: Exception =>
        val errorResponse = ErrorResponse("unknown", e.getMessage)
        cask.Response(write(errorResponse), 400)
    }
  }

  @cask.get("/heartbeat")
  def heartbeat(): String = {
    updateHeartbeat()
    "ok"
  }

  private def updateHeartbeat(): Unit = {
    lastHeartbeatTime.set(System.currentTimeMillis())
    logger.debug("Heartbeat received")
  }

  private def startWatchdog(): Unit = {
    watchdogExecutor.scheduleAtFixedRate(
      () => {
        val currentTime            = System.currentTimeMillis()
        val timeSinceLastHeartbeat = currentTime - lastHeartbeatTime.get()

        if (timeSinceLastHeartbeat > heartbeatTimeoutMs) {
          logger.warn(s"No heartbeat received in ${timeSinceLastHeartbeat}ms, shutting down")
          watchdogExecutor.shutdown()
          System.exit(1)
        }
      },
      2,
      2,
      TimeUnit.SECONDS
    )
  }

  initialize()

  override def main(args: Array[String]): Unit = {
    super.main(args)
    startWatchdog()
    logger.info(s"LLM4S Runner service started on ${host}:${port}")
    logger.info(s"Watchdog timer initialized with ${heartbeatTimeoutMs}ms timeout")
  }

  def shutdown(): Unit = {
    watchdogExecutor.shutdown()
  }
}

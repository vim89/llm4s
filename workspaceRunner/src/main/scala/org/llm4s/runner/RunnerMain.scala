package org.llm4s.runner

import org.slf4j.LoggerFactory

// https://com-lihaoyi.github.io/cask/

object RunnerMain extends cask.MainRoutes {

  val logger = LoggerFactory.getLogger(getClass)

  @cask.get("/")
  def hello() = {
    "LLM4S Runner service - please use the rest endpoint"
  }

  @cask.post("/do-thing")
  def doThing(request: cask.Request) = {
    request.text().reverse
  }

  initialize()

  override def main(args: Array[String]): Unit = {
    super.main(args)
    logger.info(s"LLM4S Runner service started on ${host}:${port}")
  }

  // default is localhost - macs bind localhost to ipv6 by default
  // so it doesn't work correctly.  This is a workaround
  // 0.0.0.0 binds to all interfaces - find for us as we are going
  // to run this in a docker container.
  // On mac you can bind to 'localhost' or 127.0.0.1 which changes
  // the behaviour but not sure what impact that has on other platforms
  override def host: String = "0.0.0.0"
}

package io.helidon.examples.quickstart.se

import io.helidon.config.Config
import io.helidon.logging.common.LogConfig
import io.helidon.webserver.WebServer
import io.helidon.webserver.http.HttpRouting

object Main:

  def main(args: Array[String]): Unit =
    // load logging configuration
    LogConfig.configureRuntime()

    // initialize global config from default configuration
    val config = Config.create
    Config.global(config)

    val server = WebServer.builder
      .config(config.get("server"))
      .routing(routing)
      .build
      .start
  
    println(s"WEB server is up! http://localhost:${server.port}/simple-greet")

  /** Updates HTTP Routing. */
  private[se] def routing(routing: HttpRouting.Builder): Unit =
    routing
      .register("/greet", new GreetService)
      .get("/simple-greet", (req, res) => res.send("Hello World!"))

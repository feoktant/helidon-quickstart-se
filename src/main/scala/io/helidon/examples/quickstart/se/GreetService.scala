package io.helidon.examples.quickstart.se

import io.helidon.config.Config
import io.helidon.examples.quickstart.se.GreetService.JSON
import io.helidon.http.Status
import io.helidon.webserver.http.{HttpRules, HttpService, ServerRequest, ServerResponse}
import jakarta.json.{Json, JsonObject}

import java.util.Collections
import java.util.concurrent.atomic.AtomicReference

/**
 * A simple service to greet you. Examples:
 * <p>
 * Get default greeting message:
 * <pre>curl -X GET http://localhost:8080/greet</pre>
 * <p>
 * Get greeting message for Joe:
 * <pre>curl -X GET http://localhost:8080/greet/Joe</pre>
 * <p>
 * Change greeting
 * <pre>curl -X PUT -H "Content-Type: application/json" -d '{"greeting" : "Howdy"}' http://localhost:8080/greet/greeting</pre>
 * <p>
 * The message is returned as a JSON object
 */
class GreetService(
  appConfig: Config = Config.global.get("app"),
) extends HttpService:
  // The config value for the key {@code greeting}.
  private val greeting = new AtomicReference[String]

  greeting.set(appConfig.get("greeting").asString.orElse("Ciao"))

  /**
   * A service registers itself by updating the routing rules.
   *
   * @param rules the routing rules.
   */
  def routing(rules: HttpRules): Unit =
    rules
      .get("/", defaultMessageHandler)
      .get("/{name}", messageHandler)
      .put("/greeting", updateGreetingHandler)

  /**
   * Return a worldly greeting message.
   *
   * @param request  the server request
   * @param response the server response
   */
  private def defaultMessageHandler(
    request: ServerRequest,
    response: ServerResponse,
  ): Unit =
    sendResponse(response, "World")

  /**
   * Return a greeting message using the name that was provided.
   *
   * @param request  the server request
   * @param response the server response
   */
  private def messageHandler(
    request: ServerRequest,
    response: ServerResponse,
  ): Unit =
    val name = request.path.pathParameters.get("name")
    sendResponse(response, name)

  private def sendResponse(response: ServerResponse, name: String): Unit =
    val msg = s"${greeting.get} $name!"
    val returnObject = JSON.createObjectBuilder.add("message", msg).build
    response.send(returnObject)

  private def updateGreetingFromJson(jo: JsonObject, response: ServerResponse): Unit =
    if (!jo.containsKey("greeting"))
      val jsonErrorObject = JSON.createObjectBuilder
        .add("error", "No greeting provided")
        .build
      response.status(Status.BAD_REQUEST_400)
        .send(jsonErrorObject)
    else
      greeting.set(jo.getString("greeting"))
      response.status(Status.NO_CONTENT_204).send()

  /**
   * Set the greeting to use in future messages.
   *
   * @param request  the server request
   * @param response the server response
   */
  private def updateGreetingHandler(
    request: ServerRequest,
    response: ServerResponse,
  ): Unit =
    updateGreetingFromJson(request.content.as(classOf[JsonObject]), response)

object GreetService:
  final val JSON = Json.createBuilderFactory(Collections.emptyMap)

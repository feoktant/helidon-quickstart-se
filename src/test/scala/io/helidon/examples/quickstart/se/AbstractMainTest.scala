package io.helidon.examples.quickstart.se

import io.helidon.http.Status
import io.helidon.webclient.http1.Http1Client
import io.helidon.webserver.http.HttpRouting
import io.helidon.webserver.testing.junit5.SetUpRoute
import jakarta.json.JsonObject
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.is
import org.junit.jupiter.api.Test

import scala.util.Using

object AbstractMainTest:
  @SetUpRoute 
  private[se] def routing(builder: HttpRouting.Builder): Unit =
    Main.routing(builder)

abstract class AbstractMainTest protected(private val client: Http1Client):
  @Test private[se] def testGreeting(): Unit =
    val response = client.get("/greet").request(classOf[JsonObject])
    assertThat(response.status, is(Status.OK_200))
    assertThat(response.entity.getString("message"), is("Hello World!"))

  @Test private[se] def testMetricsObserver(): Unit =
    Using.resource(client.get("/observe/metrics").request): response =>
      assertThat(response.status, is(Status.OK_200))

  @Test private[se] def testSimpleGreet(): Unit =
    val response = client.get("/simple-greet").request(classOf[String])
    assertThat(response.status, is(Status.OK_200))
    assertThat(response.entity, is("Hello World!"))

package io.helidon.examples.quickstart.se

import io.helidon.webserver.testing.junit5.{DirectClient, RoutingTest}

@RoutingTest 
class MainTest private[se](client: DirectClient) 
  extends AbstractMainTest(client)

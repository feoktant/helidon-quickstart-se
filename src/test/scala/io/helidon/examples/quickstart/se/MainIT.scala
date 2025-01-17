package io.helidon.examples.quickstart.se

import io.helidon.webclient.http1.Http1Client
import io.helidon.webserver.testing.junit5.ServerTest

@ServerTest 
class MainIT private[se](client: Http1Client) 
  extends AbstractMainTest(client)

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.6.3"

val helidon = "4.1.6"

lazy val root = (project in file("."))
  .settings(
    name := "helidon-example",
    libraryDependencies ++= Seq(
      "io.helidon.webserver"  % "helidon-webserver"        % helidon,
      "io.helidon.config"     % "helidon-config-yaml"      % helidon,
      "io.helidon.logging"    % "helidon-logging-jul"      % helidon,
      "io.helidon.http.media" % "helidon-http-media-jsonp" % helidon,
      "io.helidon.webserver.observe" % "helidon-webserver-observe-metrics" % helidon,
      "io.helidon.metrics"           % "helidon-metrics-system-meters"     % helidon % Runtime,

      "jakarta.json" % "jakarta.json-api" % "2.1.3",

      "org.junit.jupiter" % "junit-jupiter-api" % "5.10.3" % Test,
      "org.hamcrest" % "hamcrest-all" % "1.3" % Test,
      "io.helidon.webserver.testing.junit5" % "helidon-webserver-testing-junit5" % helidon % Test,
      "io.helidon.webclient" % "helidon-webclient" % helidon % Test,
    ),
  )

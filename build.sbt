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

      "jakarta.json" % "jakarta.json-api" % "2.1.3",
    ),
  )

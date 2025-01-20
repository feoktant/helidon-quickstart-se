import java.util.jar.Attributes.Name.CLASS_PATH

ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / scalaVersion := "3.6.3"

lazy val libsDir = settingKey[File]("Libs directory")
libsDir := target.value / "libs"

lazy val copyDependencies = taskKey[Unit]("Copies all JAR dependencies to target/libs")
copyDependencies := {
  IO.createDirectory(libsDir.value)
  (Runtime / managedClasspath).value.foreach { jar =>
    val dest = libsDir.value / jar.data.getName
    IO.copyFile(jar.data, dest)
  }
}

val helidon = "4.1.6"
lazy val root = (project in file("."))
  .settings(
    name := "helidon-quickstart-se",
    libraryDependencies ++= Seq(
      "io.helidon.webserver"  % "helidon-webserver"        % helidon,
      "io.helidon.config"     % "helidon-config-yaml"      % helidon,
      "io.helidon.logging"    % "helidon-logging-jul"      % helidon % Runtime,
      "io.helidon.http.media" % "helidon-http-media-jsonp" % helidon,
      "io.helidon.webserver.observe" % "helidon-webserver-observe-metrics" % helidon,
      "io.helidon.webserver.observe" % "helidon-webserver-observe-health"  % helidon,
      "io.helidon.metrics"           % "helidon-metrics-system-meters"     % helidon % Runtime,
      "io.helidon.health"            % "helidon-health-checks"             % helidon,

      "jakarta.json" % "jakarta.json-api" % "2.1.3",

      "org.junit.jupiter" % "junit-jupiter-api" % "5.10.3" % Test,
      "org.hamcrest" % "hamcrest-all" % "1.3" % Test,
      "io.helidon.webserver.testing.junit5" % "helidon-webserver-testing-junit5" % helidon % Test,
      "io.helidon.webclient" % "helidon-webclient" % helidon % Test,
    ),
    Compile / mainClass := Some("io.helidon.examples.quickstart.se.Main"),
    Compile / packageBin := (Compile / packageBin).dependsOn(copyDependencies).value,
    Compile / packageBin / artifactPath := target.value / s"${name.value}.jar",
    Compile / packageBin / packageOptions += {
      val classPathString = (Runtime / managedClasspath).value.files
        .map(libsDir.value.name + "/" + _.name)
        .mkString(" ")
      Package.ManifestAttributes(CLASS_PATH -> classPathString)
    }
  )

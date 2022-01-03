configure<io.vacco.oss.gitflow.GsPluginProfileExtension> { sharedLibrary(true, false) }

val api by configurations

dependencies {
  api(project(":vl-schema-fp"))
  api(project(":vl-core"))
  api("io.vacco.jtinn:jtinn:3.0.1")
  testImplementation(project(":vl-test-util"))
}

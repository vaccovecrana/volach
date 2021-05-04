configure<io.vacco.oss.gitflow.GsPluginProfileExtension> { sharedLibrary(true, true) }

val api by configurations

dependencies {
  api(project(":vl-core"))
  api("io.vacco.jtinn:jtinn:2.0.1")
  api("io.vacco.jsonbeans:jsonbeans:1.0.0")
  testImplementation(project(":vl-test-util"))
}

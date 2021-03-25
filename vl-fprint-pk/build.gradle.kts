configure<io.vacco.oss.CbPluginProfileExtension> { sharedLibrary(true, false) }

val api by configurations

dependencies {
  api(project(":vl-core"))
  api("io.vacco.jtinn:jtinn:2.0.0")
  testImplementation(project(":vl-test-util"))
}

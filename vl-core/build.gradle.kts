configure<io.vacco.oss.gitflow.GsPluginProfileExtension> { sharedLibrary(true, false) }

val api by configurations

dependencies {
  api(project(":vl-schema"))
  testImplementation(project(":vl-test-util"))
}

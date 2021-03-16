configure<io.vacco.oss.CbPluginProfileExtension> { sharedLibrary(true, false) }

val api by configurations

dependencies {
  api(project(":vl-core"))
}

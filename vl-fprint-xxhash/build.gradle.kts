configure<io.vacco.common.CbPluginProfileExtension> { sharedLibrary() }

val api by configurations

dependencies {
  api(project(":vl-core"))
  api("io.vacco.oruzka:oruzka:0.1.2")
}
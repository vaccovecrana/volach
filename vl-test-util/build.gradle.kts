configure<io.vacco.oss.gitflow.GsPluginProfileExtension> { sharedLibrary(false, true) }

val api by configurations

dependencies {
  api("io.vacco.jlame:jlame:3.100.2")
  api("io.vacco.jsonbeans:jsonbeans:0.9.1")
}

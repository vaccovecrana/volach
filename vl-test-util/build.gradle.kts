configure<io.vacco.oss.CbPluginProfileExtension> { sharedLibrary(false, true) }

val api by configurations

dependencies {
  api("io.vacco.jlame:jlame:3.100.2")
  api("com.fasterxml.jackson.core:jackson-databind:2.12.2")
}

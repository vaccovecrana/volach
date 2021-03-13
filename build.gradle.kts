plugins { id("io.vacco.oss") version "1.0.1" apply(false) }

subprojects {
  apply(plugin = "io.vacco.oss")
  group = "io.vacco.volach"
  version = "0.1.0"

  configure<io.vacco.oss.CbPluginProfileExtension> {
    addJ8Spec()
    addClasspathHell()
  }
}

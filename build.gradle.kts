plugins { id("io.vacco.oss.gitflow") version "0.9.8" apply(false) }

subprojects {
  apply(plugin = "io.vacco.oss.gitflow")
  group = "io.vacco.volach"
  version = "0.3.0"

  configure<io.vacco.oss.gitflow.GsPluginProfileExtension> {
    addJ8Spec()
    addClasspathHell()
  }

  configure<io.vacco.cphell.ChPluginExtension> {
    resourceExclusions.add("module-info.class")
  }
}

plugins { id("io.vacco.common-build") version "0.5.3" apply(false) }

subprojects {
  apply(plugin = "io.vacco.common-build")
  group = "io.vacco.volach"
  version = "0.1.0"

  configure<io.vacco.common.CbPluginProfileExtension> {
    addJ8Spec()
    addClasspathHell()
    setPublishingUrlTransform { repo -> "${repo.url}/${project.name}" }
  }
}

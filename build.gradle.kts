plugins { id("io.vacco.common-build") version "0.5.3" }

repositories {
  maven {
    url = uri("https://dl.bintray.com/vaccovecrana/vacco-oss")
  }
}

group = "io.vacco.volach"
version = "0.1.0"

configure<io.vacco.common.CbPluginProfileExtension> {
  addGoogleJavaFormat()
  addJ8Spec()
  addPmd()
  addSpotBugs()
  addClasspathHell()
  setPublishingUrlTransform { repo -> "${repo.url}/${project.name}" }
  sharedLibrary()
}

val api by configurations

dependencies {
  testImplementation("io.vacco.jlame:jlame:3.100.0")
  testImplementation("com.fasterxml.jackson.core:jackson-databind:2.12.1")
}
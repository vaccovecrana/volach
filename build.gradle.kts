plugins { id("io.vacco.common-build") version "0.5.3" }

repositories {
  maven {
    url = uri("https://dl.bintray.com/vaccovecrana/vacco-oss")
  }
}

group = "io.vacco.volach"
version = "0.1.0"

configure<io.vacco.common.CbPluginProfileExtension> {
  // addGoogleJavaFormat()
  addJ8Spec()
  addPmd()
  addSpotBugs()
  addClasspathHell()
  setPublishingUrlTransform { repo -> "${repo.url}/${project.name}" }
  sharedLibrary()
}

val api by configurations

dependencies {
  api("io.vacco.jaad:jaad:0.8.7")
  api("io.vacco.jlame:jlame:3.100.0")
  api("io.vacco.joggvorbis:joggvorbis:1.3.1")
}
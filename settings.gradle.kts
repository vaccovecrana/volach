pluginManagement {
  repositories {
    jcenter(); gradlePluginPortal()
    maven { name = "VaccoOss"; setUrl("https://dl.bintray.com/vaccovecrana/vacco-oss") }
  }
}

include("vl-core", "vl-fprint-xxhash", "vl-test")
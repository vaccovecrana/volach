pluginManagement {
  repositories {
    mavenCentral(); gradlePluginPortal()
    maven { name = "VaccoOss"; setUrl("https://vacco-oss.s3.us-east-2.amazonaws.com") }
  }
}

include("vl-core", "vl-fprint-pk", "vl-test")

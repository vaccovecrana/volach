configure<io.vacco.oss.CbPluginProfileExtension> { sharedLibrary(true, true) }

val api by configurations

dependencies {
  api(project(":vl-core"))
  api("io.vacco.jtinn:jtinn:2.0.1")
  api("com.esotericsoftware.yamlbeans:yamlbeans:1.15")
  testImplementation(project(":vl-test-util"))
}

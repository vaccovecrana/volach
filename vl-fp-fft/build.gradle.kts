val api by configurations

dependencies {
  api(project(":vl-schema"))
  api("io.vacco.jtinn:jtinn:3.0.1")
  testImplementation("io.vacco.joggvorbis:joggvorbis:1.3.6")
  testImplementation("com.google.code.gson:gson:2.8.9")
}

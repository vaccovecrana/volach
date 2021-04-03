package io.vacco.volach.fprint.pk;

import com.esotericsoftware.yamlbeans.YamlReader;
import io.vacco.jtinn.net.JtNetwork;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class VlFprintSpecUtil {

  public static interface VlUnsafeConsumer<T> {
    void accept(T t) throws Exception;
  }

  public static final String[][] sources = {
      {"/training-set-00/sample-000.mp3", "./vl-fprint-pk/build/sample-000.mp3-spectrum.json", "./vl-fprint-pk/build/sample-000.mp3-spectrum.csv"},
      {"/training-set-00/sample-001.mp3", "./vl-fprint-pk/build/sample-001.mp3-spectrum.json", "./vl-fprint-pk/build/sample-001.mp3-spectrum.csv"},
      {"/training-set-00/sample-002.mp3", "./vl-fprint-pk/build/sample-002.mp3-spectrum.json", "./vl-fprint-pk/build/sample-002.mp3-spectrum.csv"},
      {"/training-set-00/sample-003.mp3", "./vl-fprint-pk/build/sample-003.mp3-spectrum.json", "./vl-fprint-pk/build/sample-003.mp3-spectrum.csv"},
      {"/training-set-00/sample-004.mp3", "./vl-fprint-pk/build/sample-004.mp3-spectrum.json", "./vl-fprint-pk/build/sample-004.mp3-spectrum.csv"}
  };

  public static final String[][] querySources = {
      {"/query-set-00/00-reference-recording.mp3", "/query-set-00/00-live-recording-00.mp3", "/query-set-00/00-live-recording-01.mp3"},
      {"/query-set-00/01-reference-recording.mp3"},
      {"/query-set-00/02-reference-recording.mp3"},
      {"/query-set-00/03-reference-recording.mp3"},
      {"/query-set-00/04-reference-recording.mp3"},
      {"/query-set-00/05-reference-recording.mp3"},
      {"/query-set-00/06-reference-recording.mp3"},
      {"/query-set-00/07-reference-recording.mp3"},
      {"/query-set-00/08-reference-recording.mp3"},
      {"/query-set-00/09-reference-recording.mp3"},
      {"/query-set-00/10-reference-recording.mp3", "/query-set-00/10-live-recording-00.mp3"}
  };

  public static JtNetwork loadNet() throws IOException {
    URL netUrl = VlPairExtractionSpec.class.getResource("/io/vacco/volach/fprint/pk/net.yml");
    return new YamlReader(new InputStreamReader(netUrl.openStream())).read(JtNetwork.class);
  }

  public static final File outSpectrum = new File("./build/extraction-spec.csv");
  public static final File outAnchors = new File("./build/extraction-anc.csv");

  public static void withAudio(String classPath, VlUnsafeConsumer<URL> onData) {
    URL u = VlFprintSpecUtil.class.getResource(classPath);
    try {
      if (u != null) {
        onData.accept(u);
      } else {
        System.out.printf("Audio content not found: [%s]. Likely running in CI environment.%n", classPath);
      }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}

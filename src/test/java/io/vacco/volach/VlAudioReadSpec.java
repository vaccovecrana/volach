package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

import static io.vacco.volach.VlSpecUtil.withWriter;
import static j8spec.J8Spec.it;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlAudioReadSpec {

  static {
    it(
        "Can read mp3 stereo audio data, downmixing to mono",
        () -> {
          URL url = VlAudioAnalysisSpec.class.getResource("/eas-sample.mp3");
          File rawData = new File("./build/samples-raw.txt");
          withWriter(rawData, out -> {
            VlSignalExtractor.from(url).forEach(chunk -> {
              System.out.println(chunk);
              for (int k = 0; k < chunk.data.capacity(); k++) {
                out.println(chunk.data.get(k));
              }
            });
          });
        });
  }

}

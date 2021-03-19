package io.vacco.volach;

import io.vacco.volach.wavelet.*;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.dto.VlAnalysisSample;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.*;

import static j8spec.J8Spec.it;
import static io.vacco.volach.VlSpecUtil.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlAudioAnalysisSpec {

  static {
    it(
        "Can extract frequency data from audio content",
        () -> {
          File values = new File("./build/coefficients-eas-l4-frequency.txt");
          VlAnalysisParameters params = analysisParams;
          VlUpdateListener listener = new VlUpdateListener();

          params.src = new File("/Users/jjzazuet/Desktop/sample-06.mp3").toURI().toURL();

          withWriter(values, out ->
              VlWaveletPacketAnalysisExtractor.from(params).forEach(chunk -> {
                System.out.printf("Extracted [%s] wavelet packet samples from %s%n", chunk.samples.length, chunk.signal);
                for (VlAnalysisSample analysisSample : chunk.samples) {
                  listener.onData(analysisSample.freqPower, out, true);
                }
              })
          );
          listener.done();
        }
    );
  }

}

package io.vacco.volach;

import io.vacco.volach.wavelet.*;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.dto.VlAnalysisSample;
import io.vacco.volach.wavelet.type.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.Arrays;

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
          VlAnalysisParameters params = VlAnalysisParameters.from(
              // VlAudioAnalysisSpec.class.getResource("/eas.mp3");
              new File("/Users/jjzazuet/Desktop/sample.mp3").toURI().toURL(),
              65535, 10, true, new VlHaar1(), VlWpNode.Order.Sequency
          );
          float[][] freqBands = new float[1][];
          float[] range = new float[2];

          withWriter(values, out ->
              VlWaveletPacketAnalysisExtractor.from(params).forEach(chunk -> {
                System.out.printf("Extracted [%s] wavelet packet samples from %s%n", chunk.samples.length, chunk.signal);
                if (freqBands[0] == null) {
                  freqBands[0] = new float[chunk.samples[0].freqPower.capacity()];
                }
                for (VlAnalysisSample analysisSample : chunk.samples) {
                  analysisSample.freqPower.get(freqBands[0]);
                  out.println(Arrays.toString(freqBands[0]).replace("[", "").replace("]", ""));
                  for (float v : freqBands[0]) {
                    if (v < range[0]) range[0] = v;
                    if (v > range[1]) range[1] = v;
                  }
                }
              })
          );

          System.out.printf("vmin=%s, vmax=%s%n", range[0] / 32, range[1] / 32);
        }
    );
  }

}

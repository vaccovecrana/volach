package io.vacco.volach.wavelet;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.wavelet.dto.VlAudioIOParameters;
import io.vacco.volach.wavelet.dto.VlAnalysisSample;
import io.vacco.volach.wavelet.type.VlHaar1;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.*;

import static j8spec.J8Spec.it;
import static io.vacco.volach.VlAnalysisUtil.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlWaveletPacketAnalysisSpec {

  public static final VlAudioIOParameters samplingParams = VlAudioIOParameters.from(
      null, 65535, 10, true, 0, new VlHaar1(), VlWpNode.Order.Sequency
  );

  static {
    it("Can extract wavelet packet data from audio content", () -> {
      File values = new File("./build/coefficients-eas-l4-frequency.txt");
      VlAudioIOParameters params = samplingParams;
      VlUpdateListener listener = new VlUpdateListener();

      params.src = VlWaveletPacketAnalysisSpec.class.getResource("/eas.mp3");

      withWriter(values, out -> VlWaveletPacketAnalysisExtractor.from(params).forEach(chunk -> {
        System.out.printf("Extracted [%s] wavelet packet samples from %s%n", chunk.samples.length, chunk.signal);
        for (VlAnalysisSample analysisSample : chunk.samples) {
          listener.onData(analysisSample.freqPower, out, true);
        }
      })
      );
      listener.done();
    });
  }

}

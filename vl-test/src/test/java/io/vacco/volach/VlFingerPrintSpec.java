package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.*;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.dto.VlAnalysisSample;
import io.vacco.volach.wavelet.type.VlCoiflet2;
import io.vacco.volach.wavelet.type.VlDaubechies4;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;
import java.util.Arrays;

import static io.vacco.volach.VlSpecUtil.withWriter;
import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlFingerPrintSpec {

  static {
    it("Process frequency peaks from an audio signal", () -> {
      VlAnalysisParameters params = VlAnalysisParameters.from(
          new File("/Users/jjzazuet/Desktop/sample.mp3").toURI().toURL(),
          // VlFingerPrintSpec.class.getResource("/eas.mp3");
          128000, 6, true,
          new VlDaubechies4(), VlWpNode.Order.Sequency
      );
      File values = new File("./build/coefficient-peaks-eas.txt");
      double[] range = new double[2];

      withWriter(values, out ->
          VlWaveletPacketAnalysisExtractor.from(params).forEach(chunk -> {
            System.out.printf("Detecting peaks on [%s] wavelet packet samples%n", chunk.samples.length);
            for (VlAnalysisSample smp : chunk.samples) {

            }
            /*
            for (double[] frq : buffer[1]) {
              out.println(Arrays.toString(frq).replace("[", "").replace("]", ""));
              for (double v : frq) {
                if (v < range[0]) range[0] = v;
                if (v > range[1]) range[1] = v;
              }
            }*/
          })
      );
      System.out.printf("vmin=%s, vmax=%s%n", range[0] / 32, range[1] / 32);
    });
  }
}

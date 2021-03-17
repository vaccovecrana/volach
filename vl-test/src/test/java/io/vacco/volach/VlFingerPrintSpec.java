package io.vacco.volach;

import io.vacco.volach.fprint.pk.VlPeakAnalysisExtractor;
import io.vacco.volach.wavelet.*;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.type.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;

import static io.vacco.volach.VlSpecUtil.withWriter;
import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlFingerPrintSpec {

  static {
    it("Process frequency peaks from an audio signal", () -> {
      VlAnalysisParameters params = VlAnalysisParameters.from(
          new File("/Users/jjzazuet/Desktop/sample-02.mp3").toURI().toURL(),
          // VlFingerPrintSpec.class.getResource("/eas.mp3");
          131_071, 9, false,
          new VlBattle23(), VlWpNode.Order.Sequency
      );
      File values = new File("./build/coefficient-peaks-eas.txt");
      VlUpdateListener listener = new VlUpdateListener();

      withWriter(values, out ->
          VlPeakAnalysisExtractor.from(params, 128).forEach(chunk -> {
            System.out.printf("Detecting peaks on [%s] wavelet packet samples%n", chunk.length);
            for (float[] buffer : chunk) {
              listener.onData(buffer, out);
            }
          })
      );
      listener.done();
    });
  }
}

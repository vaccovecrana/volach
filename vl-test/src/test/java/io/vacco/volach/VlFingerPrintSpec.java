package io.vacco.volach;

import io.vacco.volach.fprint.VlNnFpProcessor;
import io.vacco.volach.fprint.VlNnFpSampleSupplier;
import io.vacco.volach.wavelet.*;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.type.VlCoiflet2;
import io.vacco.volach.wavelet.type.VlDaubechies4;
import io.vacco.volach.wavelet.type.VlHaar1;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.net.URL;
import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlFingerPrintSpec {
  static {
    it("Can train a network to recognize a single audio example", () -> {
      URL url = VlFingerPrintSpec.class.getResource("/eas.mp3");
      // URL url = new File("/home/jjzazuet/Desktop/comparison-sample-stereo.mp3").toURI().toURL();

      VlAnalysisParameters params = VlAnalysisParameters.from(
          url, 32768, 12, false, new VlHaar1(), VlWpNode.Order.Sequency
      );
      int trackId = 0x2d67c8d4;
      VlNnFpSampleSupplier sup = new VlNnFpSampleSupplier(() -> trackId, params);
      VlNnFpProcessor proc = new VlNnFpProcessor(sup);

      proc.trainer.start();
      System.out.println("Done");
    });
  }
}

package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.VlXXHashFingerPrint;
import io.vacco.volach.fprint.VlXXHashFingerPrintExtractor;
import io.vacco.volach.wavelet.*;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;

import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class VlFingerPrintSpec {
  static {
    it("Can identify top frequency peaks per packet sample", () -> {
      URL url = new File("/home/jjzazuet/Desktop/out.mp3").toURI().toURL();
      VlXXHashFingerPrintExtractor.from(0.6,
          new VlWaveletPacketAnalysisExtractor(
              new VlSignalExtractor(url, 32768),
              12, new VlDaubechies4(), VlWpNode.Order.Sequency
          )
      ).forEach(fingerPrints -> {
        for (VlXXHashFingerPrint fingerPrint : fingerPrints) {
          System.out.println(fingerPrint);
        }
      });
    });
  }
}

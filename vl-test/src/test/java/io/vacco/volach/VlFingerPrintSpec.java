package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.VlEnergyFingerPrint;
import io.vacco.volach.fprint.VlEnergyFingerPrintExtractor;
import io.vacco.volach.wavelet.*;
import io.vacco.volach.wavelet.type.VlHaar1;
import io.vacco.volach.wavelet.type.VlWavelet;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.net.URL;
import static io.vacco.volach.VlSpecUtil.withWriter;
import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlFingerPrintSpec {
  static {
    it("Can identify top frequency peaks per packet sample", () -> {
      int level = 12;
      VlWavelet wavelet = new VlHaar1();
      URL url = VlFingerPrintSpec.class.getResource("/eas.mp3");
      // URL url = new File("/home/jjzazuet/Desktop/comparison-sample-stereo.mp3").toURI().toURL();
      File values = new File("./build/average-sample-energy-eas.txt");

      withWriter(values, out -> VlEnergyFingerPrintExtractor.from(
          new VlWaveletPacketAnalysisExtractor(
              new VlSignalExtractor(url, 65535),
              level, wavelet, VlWpNode.Order.Sequency
          )
      ).forEach(fingerPrints -> {
        System.out.printf("Extracted [%s] fingerprints%n", fingerPrints.length);
        for (VlEnergyFingerPrint fingerPrint : fingerPrints) {
          out.println(fingerPrint.energyAvg);
        }
      }));
    });
  }
}

package io.vacco.volach.wavelet;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.schema.wavelet.VlWpNode;
import io.vacco.volach.wavelet.type.VlHaar1;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.io.File;

import static io.vacco.volach.util.VlArrays.*;
import static io.vacco.volach.VlAnalysisUtil.*;
import static j8spec.J8Spec.it;

@RunWith(J8SpecRunner.class)
public class VlEasSignalSpec {
  static {
    it(
        "Can perform wavelet packet transform analysis on a long audio signal",
        () -> {
          int level = 4;
          float[] rawSamples = loadJson("/samples-eas.json", float[].class);
          float[] samplesP2 = padPow2(rawSamples);

          VlWpNode root = VlWaveletPacketTransform.naturalTree(samplesP2, new VlHaar1(), level);
          VlWpNode[] nodes = VlWaveletPacketTransform.collect(root, level);
          float[][] coeffNatural = VlWaveletPacketTransform.extractCoefficients(nodes);
          VlUpdateListener listener = new VlUpdateListener();

          withWriter(new File("./build/coefficients-eas-sample-l4-natural.txt"), out -> {
            for (float[] b : coeffNatural) {
              listener.onData(b, out, false);
            }
          });
        }
    );
  }
}

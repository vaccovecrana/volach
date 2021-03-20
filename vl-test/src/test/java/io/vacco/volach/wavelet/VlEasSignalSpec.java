package io.vacco.volach.wavelet;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.type.VlHaar1;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.FloatBuffer;

import static io.vacco.volach.util.VlArrays.*;
import static io.vacco.volach.VlSpecUtil.*;
import static j8spec.J8Spec.it;

@RunWith(J8SpecRunner.class)
public class VlEasSignalSpec {
  static {
    it(
        "Can perform wavelet packet transform analysis on a long audio signal",
        () -> {
          int level = 4;

          float[] rawSamples = mapper.readValue(VlLinchirpSpec.class.getResource("/sample-data/samples-eas.json"), float[].class);
          FloatBuffer samples = floatBuffer(rawSamples.length).put(rawSamples);
          FloatBuffer samplesP2 = VlSignalExtractor.padPow2(samples);

          VlWpNode root = VlWaveletPacketTransform.naturalTree(samplesP2, new VlHaar1(), level);
          VlWpNode[] nodes = VlWaveletPacketTransform.collect(root, level);
          FloatBuffer[] coeffNatural = VlWaveletPacketTransform.extractCoefficients(nodes);
          VlUpdateListener listener = new VlUpdateListener();

          withWriter(new File("./build/coefficients-eas-sample-l4-natural.txt"), out -> {
            for (FloatBuffer b : coeffNatural) {
              listener.onData(b, out, false);
            }
          });
        }
    );
  }
}

package io.vacco.volach.wavelet;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.type.VlHaar1;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static io.vacco.volach.util.VlArrays.*;
import static io.vacco.volach.VlSpecUtil.*;
import static j8spec.J8Spec.it;
import static org.junit.Assert.*;

@RunWith(J8SpecRunner.class)
public class VlLinchirpSpec {

  static {
    it(
        "Can perform wavelet packet transform analysis on a linear chirp audio signal",
        () -> {
          int level = 4;

          float[] rawSamples = mapper.readValue(VlLinchirpSpec.class.getResource("/sample-data/samples-linchirp.json"), float[].class);
          float[][] refCoefficientsLevel4Natural = mapper.readValue(VlLinchirpSpec.class.getResource("/sample-data/coefficients-linchirp-l4-natural.json"), float[][].class);
          float[][] refCoefficientsLevel4Frequency = mapper.readValue(VlLinchirpSpec.class.getResource("/sample-data/coefficients-linchirp-l4-frequency.json"), float[][].class);

          FloatBuffer samples = floatBuffer(rawSamples.length).put(rawSamples);
          FloatBuffer samplesP2 = VlSignalExtractor.padPow2(samples);

          VlWpNode root = VlWaveletPacketTransform.naturalTree(samplesP2, new VlHaar1(), level);
          VlWpNode[] nodes = VlWaveletPacketTransform.collect(root, level);
          FloatBuffer[] coeffNatural = VlWaveletPacketTransform.extractCoefficients(nodes);

          System.out.println("================= Audio signal - Frq data (L4 - Natural) =================");
          print2d(coeffNatural);

          System.out.println("================= Audio signal - Ref data (L4 - Natural) =================");
          print2d(refCoefficientsLevel4Natural);

          assertTrue(coeffNatural[0].get(0) > 0.8984);
          assertTrue(coeffNatural[coeffNatural.length - 1].get(0) < -0.0015);

          root.asSequencyMutation();

          System.out.println("================= Audio signal - Frq data (L4 - Frequency) =================");
          FloatBuffer[] coeffFreq = VlWaveletPacketTransform.extractCoefficients(VlWaveletPacketTransform.collect(root, level));
          print2d(coeffFreq);

          assertTrue(coeffFreq[0].get(0) > 0.8984);
          assertTrue(coeffFreq[coeffFreq.length - 1].get(0) < -0.0826);

          System.out.println("================= Audio signal - Ref data (L4 - Frequency) =================");
          print2d(refCoefficientsLevel4Frequency);

          VlUpdateListener listener = new VlUpdateListener();
          withWriter(
              new File("./build/coefficients-linchirp-l4-frequency.txt"),
              out -> Arrays.stream(coeffFreq).forEach(b -> listener.onData(b, out, false))
          );
          listener.done();
        }
    );
  }
}
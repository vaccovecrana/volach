package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.VlDaubechies4;
import io.vacco.volach.wavelet.VlHaar1;
import io.vacco.volach.wavelet.VlWaveletPacketTransform;
import io.vacco.volach.wavelet.VlWpNode;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static io.vacco.volach.audioio.VlArrays.*;
import static io.vacco.volach.VlSpecUtil.*;
import static j8spec.J8Spec.it;
import static org.junit.Assert.*;

@RunWith(J8SpecRunner.class)
public class VlSignalSpec {

  static {
    it(
        "Can perform wavelet packet transform analysis on an audio signal",
        () -> {
          /**
           * For a 2^N input signal array, the transform at a certain level will yield analysis for at most
           * some lowest power of two in terms of samples, so the audio signal extractor must account for that
           * as well.
           *
           * For the Haar1 wavelet:
           * 512smp @ lv1 -> 256 smp,   2   freq bands
           * 512smp @ lv4 -> 32  smp,   16  freq bands
           * 512smp @ lv8 -> 2   smp,   256 freq bands
           * 512smp @ lv9 -> undefined
           *
           * For the Daubechies4 wavelet:
           * 512smp @ lv1 -> 256 smp,   2   freq bands
           * 512smp @ lv4 -> 32  smp,   16  freq bands
           * 512smp @ lv5 -> 16  smp,   32  freq bands
           * 512smp @ lv6 -> 8   smp,   64  freq bands
           * 512smp @ lv7 -> undefined
           *
           * For the Daubechies16 wavelet:
           * 512smp @ lv1 -> 256 smp,   2   freq bands
           * 512smp @ lv4 -> 32  smp,   16  freq bands
           * 512smp @ lv5 -> undefined
           * 512smp @ lv6 -> undefined
           *
           * Looks like the mother wavelength attribute of the wavelet describes the highest amount of samples
           * that can be decomposed for some amount of input data.
           *
           * TODO take the input signal length, bit shift to the right {level} times. If the result is less than
           * the mother wavelet length, then that decomposition level is undefined.
           *
           * TODO this tells us how many analysis samples will be available on the frequency domain. Would this
           * require a sliding window then??
           */
          int level = 4;

          float[] rawSamples = mapper.readValue(VlSignalSpec.class.getResource("/samples-linchirp.json"), float[].class);
          float[][] refCoefficientsLevel4Natural = mapper.readValue(VlSignalSpec.class.getResource("/coefficients-linchirp-l4-natural.json"), float[][].class);
          float[][] refCoefficientsLevel4Frequency = mapper.readValue(VlSignalSpec.class.getResource("/coefficients-linchirp-l4-frequency.json"), float[][].class);

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

          float[] range = new float[2];
          float[][] buffer = new float[1][];
          File chirpCoefficients = new File("./build/coefficients-linchirp-l4-frequency.txt");

          withWriter(chirpCoefficients, out -> {
            for (FloatBuffer floatBuffer : coeffFreq) {
              if (buffer[0] == null) {
                buffer[0] = new float[floatBuffer.capacity()];
              }
              floatBuffer.get(buffer[0]);
              out.println(Arrays.toString(buffer[0]).replace("[", "").replace("]", ""));
              for (float v : buffer[0]) {
                if (v < range[0]) range[0] = v;
                if (v > range[1]) range[1] = v;
              }
            }
          });

          System.out.printf("vmin=%s, vmax=%s", range[0] / 8, range[1] / 8);
        }
    );
  }
}

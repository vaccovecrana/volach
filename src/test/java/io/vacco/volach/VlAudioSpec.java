package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.VlCoiflet2;
import io.vacco.volach.wavelet.VlWaveletPacketTransform;
import io.vacco.volach.wavelet.VlHaar1;
import io.vacco.volach.wavelet.VlWpNode;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import javax.sound.sampled.AudioInputStream;
import java.nio.FloatBuffer;

import static io.vacco.volach.audioio.VlArrays.*;
import static io.vacco.volach.audioio.VlMonoAudioInputStream.loadPcm16Le;
import static j8spec.J8Spec.it;

import static org.junit.Assert.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlAudioSpec {

  static {
    it(
        "Can read mp3 audio data",
        () -> {
          byte[] buffer = new byte[4096];
          int read, total = 0;
          AudioInputStream min = loadPcm16Le(VlAudioSpec.class.getResource("/250Hz_44100Hz_16bit_03sec.mp3"));
          while ((read = min.read(buffer, 0, buffer.length)) >= 0) {
            System.err.printf(".");
            total += read;
          }
          System.err.println();
          System.err.printf("Read %s bytes%n", total);
        });
    it(
        "Can perform wavelet packet transform analysis on audio data",
        () -> {
          FloatBuffer samples = from(VlChirpSignal.samplesD);
          FloatBuffer samplesP2 = VlSignalExtractor.padPow2(samples);
          int level = 4;

          VlWpNode root = VlWaveletPacketTransform.naturalTree(samplesP2, new VlHaar1(), level);

          System.out.println("================= Frq data (L4 - Natural) =================");
          FloatBuffer[] coeffNatural = VlWaveletPacketTransform.extractCoefficients(VlWaveletPacketTransform.collect(root, level));
          print2d(coeffNatural);

          System.out.println("================= Ref data (L4 - Natural) =================");
          print2d(VlChirpSignal.refCoefficientsLevel4Natural);

          assertTrue(coeffNatural[0].get(0) > 0.8984);
          assertTrue(coeffNatural[coeffNatural.length - 1].get(0) < -0.0015);

          root.asSequencyMutation();

          System.out.println("================= Frq data (L4 - Frequency) =================");
          FloatBuffer[] coeffFreq = VlWaveletPacketTransform.extractCoefficients(VlWaveletPacketTransform.collect(root, level));
          print2d(coeffFreq);

          assertTrue(coeffFreq[0].get(0) > 0.8984);
          assertTrue(coeffFreq[coeffFreq.length - 1].get(0) < -0.0826);

          System.out.println("================= Ref data (L4 - Frequency) =================");
          print2d(VlChirpSignal.refCoefficientsLevel4Frequency);
        }
    );
  }

  private static FloatBuffer from(double[] in) {
    FloatBuffer out = floatBuffer(in.length);
    for (int i = 0; i < in.length; i++) {
      out.put(i, (float) in[i]);
    }
    return out;
  }

  private static void print2d(FloatBuffer[] in) {
    for (int i = 0; i < in.length; i++) {
      FloatBuffer data = in[i];
      for (int k = 0; k < data.capacity(); k++) {
        System.out.printf("[%s, %s, %s], %n", i, k, data.get(k));
      }
    }
  }

  private static void print2d(double[][] in) {
    for (int i = 0; i < in.length; i++) {
      double[] data = in[i];
      for (int k = 0; k < data.length; k++) {
        System.out.printf("[%s, %s, %s], %n", i, k, data[k]);
      }
    }
  }
}

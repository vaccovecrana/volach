package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.VlDwPt;
import io.vacco.volach.wavelet.VlHaar1;
import io.vacco.volach.wavelet.VlWpNode;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import javax.sound.sampled.AudioInputStream;

import static io.vacco.volach.audioio.VlMonoAudioInputStream.loadPcm16Le;
import static j8spec.J8Spec.it;

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
        "Can normalize audio data",
        () -> {
          // float[] samples = VlSignalExtractor.apply(VlAudioSpec.class.getResource("/mactonite-piano-piece-6-winter-lights.mp3"));

          float[] samples = from(VlChirpSignal.samplesD);
          float[] samplesP2 = VlSignalExtractor.padPow2(samples);

          VlWpNode root = VlDwPt.extract(samplesP2, new VlHaar1(), 5);

          float[][] freqData = root.collectLeafSamples(samples.length);

          System.out.println("================= Frq data (L5) =================");
          print2d(freqData);

          System.out.println("================= Ref data (L5) =================");
          print2d(VlChirpSignal.refCoefficientsLevel5);
        }
    );
  }

  private static float[] from(double[] in) {
    float[] out = new float[in.length];
    for (int i = 0; i < in.length; i++) {
      out[i] = (float) in[i];
    }
    return out;
  }

  private static void print2d(float[][] in) {
    for (int i = 0; i < in.length; i++) {
      float[] data = in[i];
      for (int k = 0; k < data.length; k++) {
        System.out.printf("[%s, %s, %s], %n", i, k, data[k]);
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

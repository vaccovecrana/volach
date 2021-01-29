package io.vacco.volach;

import static j8spec.J8Spec.*;

import io.vacco.volach.audioio.VlSignalExtractor;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;

import javax.sound.sampled.*;
import org.junit.runner.RunWith;

import static io.vacco.volach.audioio.VlMonoAudioInputStream.*;

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
          float[] samples = VlSignalExtractor.apply(VlAudioSpec.class.getResource("/mactonite-piano-piece-6-winter-lights.mp3"));
          // for (float sample : samples) { System.out.printf("%.8f%n", sample); }
          System.out.println();
        }
    );
  }
}

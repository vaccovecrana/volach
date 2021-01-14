package io.vacco.volach;

import static j8spec.J8Spec.*;

import io.vacco.volach.audioio.VlMonoAudioInputStream;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import java.net.URL;
import javax.sound.sampled.*;
import org.junit.runner.RunWith;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class AudioSpec {

  private static void readAudio(URL url) throws Exception {
    AudioInputStream in = AudioSystem.getAudioInputStream(url);
    AudioFormat in_format = in.getFormat();
    int channels = in_format.getChannels();

    System.out.println(in_format);

    AudioFormat decoded_format =
        new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            in_format.getSampleRate(),
            16,
            channels,
            channels * (16 / 8),
            in_format.getSampleRate(),
            false);

    System.out.println(decoded_format);

    VlMonoAudioInputStream min =
        new VlMonoAudioInputStream(AudioSystem.getAudioInputStream(decoded_format, in));
    final byte[] buffer = new byte[4096];
    int read, total = 0;

    while ((read = min.read(buffer, 0, buffer.length)) >= 0) {
      System.err.printf(".");
      total += read;
    }
    System.err.println();
    System.err.printf("Read %s bytes%n", total);
  }

  static {
    it(
        "Can read mp3 audio data",
        () -> readAudio(AudioSpec.class.getResource("/250Hz_44100Hz_16bit_03sec.mp3")));
  }
}

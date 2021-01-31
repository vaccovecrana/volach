package io.vacco.volach.audioio;

import java.net.URL;
import java.nio.FloatBuffer;

import static io.vacco.volach.audioio.VlMonoAudioInputStream.processPcm16Le;
import static io.vacco.volach.audioio.VlArrays.*;

/**
 * TODO this class could use enhancements, namely the capability
 * to read, normalize and provide sample data in a streaming fashion
 * (for longer audio content).
 */
public class VlSignalExtractor {

  public static FloatBuffer apply(URL url) {
    int[] counters = new int[2];
    processPcm16Le(url, sample -> counters[0] = counters[0] + 1);
    FloatBuffer signal = floatBuffer(counters[0]);
    processPcm16Le(url, sample -> {
      float val = sample; // (((sample * 2) + 1) / (float) 65535);
      signal.put(counters[1], val);
      counters[1] = counters[1] + 1;
    });
    return signal;
  }

  public static FloatBuffer padPow2(FloatBuffer in) {
    FloatBuffer out = floatBuffer(nextPow2(in.capacity()));
    copy(in, 0, out, 0, in.capacity());
    return out;
  }

  public static int nextPow2(int x) {
    return x == 1 ? 1 : Integer.highestOneBit(x - 1) * 2;
  }

}

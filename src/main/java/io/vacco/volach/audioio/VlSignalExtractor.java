package io.vacco.volach.audioio;

import java.net.URL;
import static io.vacco.volach.audioio.VlMonoAudioInputStream.*;

/**
 * TODO this class could use enhancements, namely the capability
 * to read, normalize and prove sample data in a streaming fashion
 * (for longer audio content).
 */
public class VlSignalExtractor {

  public static float[] apply(URL url) {
    int[] counters = new int[2];
    processPcm16Le(url, sample -> counters[0] = counters[0] + 1);
    float[] signal = new float[counters[0]];
    processPcm16Le(url, sample -> {
      float val = (((sample * 2) + 1) / (float) 65535);
      signal[counters[1]] = val;
      counters[1] = counters[1] + 1;
    });
    return signal;
  }

}

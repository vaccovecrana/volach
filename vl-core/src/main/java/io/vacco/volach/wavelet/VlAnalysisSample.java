package io.vacco.volach.wavelet;

import java.nio.FloatBuffer;
import static io.vacco.volach.util.VlArrays.*;

public class VlAnalysisSample {

  public int level;
  public FloatBuffer freqPower;

  public static VlAnalysisSample from(int level, FloatBuffer freqPower) {
    VlAnalysisSample sample = new VlAnalysisSample();
    sample.freqPower = freqPower;
    sample.level = level;
    return sample;
  }

  public static VlAnalysisSample[] from(VlWpNode[] packets) {
    int level = packets[0].level;
    int samples = packets[0].coefficients.capacity();
    int frequencies = packets.length;
    VlAnalysisSample[] out = new VlAnalysisSample[samples];

    for (int k = 0; k < samples; k++) {
      FloatBuffer freqPower = floatBuffer(frequencies);
      for (int n = 0; n < frequencies; n++) {
        int idx = (frequencies - 1) - n;
        freqPower.put(idx, packets[n].coefficients.get(k));
      }
      out[k] = VlAnalysisSample.from(level, freqPower);
    }
    return out;
  }

}

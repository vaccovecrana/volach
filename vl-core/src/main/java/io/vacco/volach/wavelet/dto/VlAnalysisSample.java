package io.vacco.volach.wavelet.dto;

import io.vacco.volach.wavelet.VlWpNode;

import java.nio.FloatBuffer;
import static io.vacco.volach.util.VlArrays.*;

public class VlAnalysisSample {

  public int hilbertOffset;
  public FloatBuffer freqPower;

  public static VlAnalysisSample[] from(int hilbertOffset, VlWpNode[] packets) {
    int samples = packets[0].coefficients.capacity();
    int frequencies = packets.length;
    VlAnalysisSample[] out = new VlAnalysisSample[samples];

    for (int k = 0; k < samples; k++) {
      FloatBuffer freqPower = floatBuffer(frequencies);
      for (int n = 0; n < frequencies; n++) {
        int idx = (frequencies - 1) - n;
        freqPower.put(idx, packets[n].coefficients.get(k));
      }
      VlAnalysisSample sample = new VlAnalysisSample();
      sample.freqPower= freqPower;
      sample.hilbertOffset = hilbertOffset + k;
      out[k] = sample;
    }
    return out;
  }

}
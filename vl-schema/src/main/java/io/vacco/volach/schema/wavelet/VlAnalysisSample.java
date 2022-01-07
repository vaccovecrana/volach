package io.vacco.volach.schema.wavelet;

public class VlAnalysisSample {

  public int hilbertOffset;
  public float[] freqPower;

  public static VlAnalysisSample[] from(int hilbertOffset, VlWpNode[] packets) {
    int samples = packets[0].coefficients.length;
    int frequencies = packets.length;

    VlAnalysisSample[] out = new VlAnalysisSample[samples];

    for (int k = 0; k < samples; k++) {
      float[] freqPower = new float[frequencies];
      for (int n = 0; n < frequencies; n++) {
        freqPower[n] = packets[n].coefficients[k];
      }
      VlAnalysisSample sample = new VlAnalysisSample();
      sample.freqPower= freqPower;
      sample.hilbertOffset = hilbertOffset + k;
      out[k] = sample;
    }
    return out;
  }

  @Override
  public String toString() {
    return String.format(
        "smp[@hilbertOffset: %d, freqPow: %d]",
        hilbertOffset,
        freqPower != null ? freqPower.length : -1
    );
  }
}

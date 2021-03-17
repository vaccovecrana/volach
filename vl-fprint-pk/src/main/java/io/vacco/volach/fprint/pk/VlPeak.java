package io.vacco.volach.fprint.pk;

public class VlPeak {
  byte freqBand;
  float magnitude;
  int sampleOffset, hilbertOffset;

  public static VlPeak from(byte freqBand, float magnitude, int sampleOffset, int hilbertOffset) {
    VlPeak pk = new VlPeak();
    pk.freqBand = freqBand;
    pk.magnitude = magnitude;
    pk.sampleOffset = sampleOffset;
    pk.hilbertOffset = hilbertOffset;
    return pk;
  }
}

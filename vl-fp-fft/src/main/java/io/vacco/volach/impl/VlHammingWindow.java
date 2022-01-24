package io.vacco.volach.impl;

public class VlHammingWindow {

  public static final float TWO_PI = (float) (2 * Math.PI);

  private final float[] curve;
  private final float[] buff;

  public VlHammingWindow(int sampleSize) {
    this.buff = new float[sampleSize];
    this.curve = new float[sampleSize];
    for (int n = 0; n < sampleSize; n++) {
      curve[n] = 0.54f - 0.46f * (float) Math.cos(TWO_PI * n / (sampleSize - 1));
    }
  }

  public float[] update(float[] in) {
    System.arraycopy(in, 0, buff, 0, buff.length);
    for (int i = 0; i < buff.length; i++) {
      buff[i] = buff[i] * curve[i];
    }
    return buff;
  }

}

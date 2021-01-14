package io.vacco.volach.dsp;

public class HammingWindow {

  public static final float TWO_PI = (float) (2 * Math.PI);

  public static float value(int length, int index) {
    return 0.54f - 0.46f * (float) Math.cos(TWO_PI * index / (length - 1));
  }

  public static float[] generateCurve(int length) {
    float[] samples = new float[length];
    for (int n = 0; n < length; n++) {
      samples[n] = value(length, n);
    }
    return samples;
  }
}

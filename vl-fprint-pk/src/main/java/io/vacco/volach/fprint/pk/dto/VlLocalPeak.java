package io.vacco.volach.fprint.pk.dto;

public class VlLocalPeak {

  public int x, y;
  public float value;

  public static VlLocalPeak maxLocal(float[][] in) {
    int x = 0, y = 0;
    float max = 0;
    for (int j = 0; j < in.length; j++) {
      for (int k = 0; k < in[0].length; k++) {
        if (in[j][k] > max) {
          max = in[j][k];
          x = j;
          y = k;
        }
      }
    }

    VlLocalPeak peak = new VlLocalPeak();
    peak.x = x;
    peak.y = y;
    peak.value = max;
    return peak;
  }

  @Override
  public String toString() {
    return String.format("{v: %.6f, x: %d, y: %d}", value, x, y);
  }
}

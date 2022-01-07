package io.vacco.volach;

import java.io.PrintWriter;

public class VlUpdateListener {

  public float[] range = new float[2];
  public int total;

  public void onData(float[] raw, PrintWriter out, boolean absoluteMagnitudes) {
    total++;
    if (absoluteMagnitudes) {
      for (int i = 0; i < raw.length; i++) {
        raw[i] = Math.abs(raw[i]);
      }
    }
    if (out != null) {
      for (int i = 0; i < raw.length; i++) {
        out.printf("%.9f%s", raw[i], i == raw.length - 1 ? "" : ", ");
      }
      out.println();
    }
    for (float v : raw) {
      if (v < range[0]) range[0] = v;
      if (v > range[1]) range[1] = v;
    }
  }

  public void done() {
    System.out.printf("vmin=%s, vmax=%s%n", range[0] / 8, range[1] / 8);
  }
}

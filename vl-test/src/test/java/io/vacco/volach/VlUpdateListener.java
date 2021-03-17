package io.vacco.volach;

import java.io.PrintWriter;
import java.nio.FloatBuffer;
import java.util.Arrays;

public class VlUpdateListener {

  public float[] buffer;
  public float[] range = new float[2];

  public void onData(float[] raw, PrintWriter out) {
    out.println(Arrays.toString(raw).replace("[", "").replace("]", ""));
    for (float v : raw) {
      if (v < range[0]) range[0] = v;
      if (v > range[1]) range[1] = v;
    }
  }

  public void onData(FloatBuffer data, PrintWriter out) {
    if (buffer == null) { buffer = new float[data.capacity()]; }
    data.get(buffer);
    onData(buffer, out);
  }

  public void done() {
    System.out.printf("vmin=%s, vmax=%s%n", range[0] / 16, range[1] / 16);
  }
}

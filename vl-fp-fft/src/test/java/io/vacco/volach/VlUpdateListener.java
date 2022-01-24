package io.vacco.volach;

import java.io.*;
import java.util.Objects;
import java.util.function.Consumer;

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
        out.printf("%.8f%s", raw[i], i == raw.length - 1 ? "" : ", ");
      }
      out.println();
    }
    for (float v : raw) {
      if (v < range[0]) range[0] = v;
      if (v > range[1]) range[1] = v;
    }
  }

  public void done() {
    float dispFactor = 2;
    System.out.printf("vmin=%s, vmax=%s%n", range[0] / dispFactor, range[1] / dispFactor);
  }

  public Reader loadRes(String classPath) {
    return new InputStreamReader(Objects.requireNonNull(VlStFtExtTest.class.getResourceAsStream(classPath)));
  }

  public void withWriter(File out, Consumer<PrintWriter> pCons) {
    try {
      PrintWriter p = new PrintWriter(new FileWriter(out));
      pCons.accept(p);
      p.close();
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }
}

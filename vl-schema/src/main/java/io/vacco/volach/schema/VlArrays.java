package io.vacco.volach.schema;

public class VlArrays {

  public static int indexOf(float[] array, float v) {
    for (int i = 0; i < array.length; i++) {
      if (array[i] == v) return i;
    }
    return -1;
  }

  public static float min(float[] in, float m) {
    for (float v : in) {
      m = Math.min(v, m);
    }
    return m;
  }

  public static float max(float[] in, float m) {
    for (float v : in) {
      m = Math.max(v, m);
    }
    return m;
  }

  public static void normalize(float[] in, float min, float max) {
    for (int i = 0; i < in.length; i++) {
      in[i] = (in[i] - min) / (max - min);
    }
  }

  public static void regionSquare(float[][] in, int i, int j, float[][] out) {
    int size = out.length;
    int half = size / 2;
    int tl0 = i - half, tl1 = j - half;
    for (int k = 0; k < size; k++) {
      System.arraycopy(in[k + tl0], tl1, out[k], 0, size);
    }
  }

  public static void flatten(float[][] in, double[] out) {
    double val;
    int idx;
    for (int j = 0; j < in.length; j++) {
      for (int k = 0; k < in[0].length; k++) {
        idx = (j * in.length) + k;
        val = in[j][k];
        out[idx] = val;
      }
    }
  }

}

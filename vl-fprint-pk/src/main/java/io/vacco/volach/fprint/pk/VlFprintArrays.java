package io.vacco.volach.fprint.pk;

public class VlFprintArrays {

  public static void regionSquare(float[][] in, int i, int j, float[][] out) {
    int size = out.length;
    int half = size / 2;
    int tl0 = i - half, tl1 = j - half;
    for (int k = 0; k < size; k++) {
      System.arraycopy(in[k + tl0], tl1, out[k], 0, size);
    }
  }

  public static void copy(float[][] in, float[][] out) {
    for (int i = 0; i < in.length; i++) {
      System.arraycopy(in[i], 0, out[i], 0, in[0].length);
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

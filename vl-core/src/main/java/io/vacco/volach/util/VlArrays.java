package io.vacco.volach.util;

import static io.vacco.volach.util.VlMath.nextPow2;

public class VlArrays {

  public static int readSignedLe(byte[] in) {
    return (in[0] & 0xff) | (short) (in[1] << 8);
  }

  public static float[] padPow2(float[] in) {
    float[] out = new float[nextPow2(in.length)];
    System.arraycopy(in, 0, out, 0, in.length);
    return out;
  }

}

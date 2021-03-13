package io.vacco.volach.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class VlArrays {

  public static FloatBuffer floatBuffer(int size) {
    ByteBuffer bb = ByteBuffer.allocateDirect(size * 4);
    bb.order(ByteOrder.nativeOrder());
    FloatBuffer fb = bb.asFloatBuffer();
    fb.position(0);
    return fb;
  }

  // TODO this needs to be optimized :P
  public static void copy(FloatBuffer src, int srcPos, FloatBuffer dest, int destPos, int length) {
    int srcIdx, dstIdx;
    float value;
    for (int k = 0; k < length; k++) {
      srcIdx = k + srcPos;
      dstIdx = k + destPos;
      value = src.get(srcIdx);
      dest.put(dstIdx, value);
    }
  }

  public static FloatBuffer copyOfRange(FloatBuffer original, int from, int to) {
    int newLength = to - from;
    if (newLength < 0) {
      throw new IllegalArgumentException(from + " > " + to);
    } else {
      FloatBuffer copy = floatBuffer(newLength);
      copy(original, from, copy, 0, Math.min(original.capacity() - from, newLength));
      return copy;
    }
  }

  public static int readSignedLe(byte[] in) {
    return (in[0] & 0xff) | (short) (in[1] << 8);
  }

  public static void toBits32(int x, double[] dest) {
    for (int i = 31; i >= 0 ; i--) {
      int mask = 1 << i;
      dest[i] = (x & mask) != 0 ? 1 : 0;
    }
    reverse(dest);
  }

  public static int fromBits32(double[] bits) {
    int n = 0;
    for (double bit : bits) {
      n = (n << 1) + (bit >= 0.5 ? 1 : 0);
    }
    return n;
  }

  public static void packPair(int v0, int v1, double[] v0d, double[] v1d, double[] out) {
    toBits32(v0, v0d);
    toBits32(v1, v1d);
    System.arraycopy(v0d, 0, out, 0, 32);
    System.arraycopy(v1d, 0, out, 32, 32);
  }

  public static void unpackPair(double[] bits, double[] v0b, double[] v1b, int[] out) {
    System.arraycopy(bits, 0, v0b, 0, v0b.length);
    System.arraycopy(bits, 32, v1b, 0, v1b.length);
    out[0] = fromBits32(v0b);
    out[1] = fromBits32(v1b);
  }

  public static void reverse(double[] input) {
    if(input == null || input.length <= 1){ return; }
    double tmp;
    for (int i = 0; i < input.length / 2; i++) {
      tmp = input[i];
      input[i] = input[input.length - 1 - i];
      input[input.length - 1 - i] = tmp;
    }
  }

}

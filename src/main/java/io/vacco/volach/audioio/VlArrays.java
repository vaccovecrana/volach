package io.vacco.volach.audioio;

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
    for (int k = 0; k < length; k++) {
      srcIdx = k + srcPos;
      dstIdx = k + destPos;
      dest.put(dstIdx, src.get(srcIdx));
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
}

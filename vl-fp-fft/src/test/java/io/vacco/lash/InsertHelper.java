package io.vacco.lash;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

//This is used by tests only - it should be part of the testing
//packages, but we ran into some issues with Maven / Eclipse classpath
//conflicts.
public class InsertHelper {
  public static byte[] longToBytes(long i) {
    final ByteBuffer buf = ByteBuffer.allocate(8);
    buf.order(ByteOrder.nativeOrder());
    buf.putLong(i);
    return buf.array();
  }

  public static long bytesToLong(byte[] b) {
    if (b == null)
      return -1;
    final ByteBuffer buf = ByteBuffer.wrap(b);
    buf.order(ByteOrder.nativeOrder());
    return buf.getLong();
  }
}
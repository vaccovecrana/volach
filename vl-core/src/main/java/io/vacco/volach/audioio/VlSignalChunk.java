package io.vacco.volach.audioio;

import java.nio.FloatBuffer;

public class VlSignalChunk {
  public final FloatBuffer data;
  public final int offset;

  public VlSignalChunk(FloatBuffer data, int offset) {
    this.data = data;
    this.offset = offset;
  }

  @Override
  public String toString() {
    return String.format("chunk[length: %s, offset: %s]", data.capacity(), offset);
  }
}

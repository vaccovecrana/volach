package io.vacco.volach.audioio;

import java.nio.FloatBuffer;

public class VlSignalChunk {
  public final FloatBuffer data;
  public final long offset;

  public VlSignalChunk(FloatBuffer data, long offset) {
    this.data = data;
    this.offset = offset;
  }

  @Override
  public String toString() {
    return String.format("chunk[length: %s, offset: %s]", data.capacity(), offset);
  }
}

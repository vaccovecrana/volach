package io.vacco.volach.audioio;

import java.nio.FloatBuffer;

public class VlSignalChunk {
  public FloatBuffer data;
  public int id;
  public long offset;

  public VlSignalChunk(FloatBuffer data, int id, long offset) {
    this.id = id;
    this.data = data;
    this.offset = offset;
  }

  @Override
  public String toString() {
    return String.format("chunk[id: %s, offset: %s]", id, offset);
  }
}

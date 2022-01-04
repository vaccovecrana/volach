package io.vacco.volach.audioio;

import java.nio.FloatBuffer;

public class VlSignalChunk {
  public final FloatBuffer data;
  public final int sampleOffset;

  public VlSignalChunk(FloatBuffer data, int sampleOffset) {
    this.data = data;
    this.sampleOffset = sampleOffset;
  }

  @Override
  public String toString() {
    return String.format("chunk[length: %s, smpOffset: %s]", data.capacity(), sampleOffset);
  }
}

package io.vacco.volach.schema.audio;

public class VlSignalChunk {

  public final float[] data;
  public final long sampleOffset;

  public VlSignalChunk(float[] data, long sampleOffset) {
    this.data = data;
    this.sampleOffset = sampleOffset;
  }

  @Override
  public String toString() {
    return String.format("chunk[length: %s, smpOffset: %s]", data.length, sampleOffset);
  }
}

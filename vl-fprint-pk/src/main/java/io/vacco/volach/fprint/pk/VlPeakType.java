package io.vacco.volach.fprint.pk;

public enum VlPeakType {

  Noise(new float[] {0, 0, 0}),
  Transient(new float[] {0, 0, 1}),
  Harmonic(new float[] {0, 1, 0});

  public final float[] flags;

  VlPeakType(float[] flags) { this.flags = flags; }

}

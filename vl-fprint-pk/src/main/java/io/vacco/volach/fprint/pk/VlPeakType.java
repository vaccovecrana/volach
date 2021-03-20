package io.vacco.volach.fprint.pk;

public enum VlPeakType {

  Transient(new float[] {0, 0, 0}),
  TonalStraight(new float[] {0, 1, 0}),
  TonalShift(new float[] {0, 1, 1});

  public final float[] flags;

  VlPeakType(float[] flags) { this.flags = flags; }

}

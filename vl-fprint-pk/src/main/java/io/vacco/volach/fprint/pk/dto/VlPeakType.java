package io.vacco.volach.fprint.pk.dto;

public enum VlPeakType {

  Transient(new double[]     {0, 0, 1}),
  TonalStraight(new double[] {0, 1, 0}),
  TonalShift(new double[]    {1, 0, 0});

  public final double[] flags;

  VlPeakType(double[] flags) { this.flags = flags; }

  public static VlPeakType fromRaw(double[] flags) {
    for (int k = 0; k < flags.length; k++) {
      flags[k] = flags[k] > 0.88 ? 1 : 0;
    }
    if (flags[0] == 1) return TonalShift;
    else if (flags[1] == 1) return TonalStraight;
    else if (flags[2] == 1) return Transient;
    return null;
  }
}

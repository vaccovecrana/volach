package io.vacco.volach.schema.fprint;

public enum VlPeakType {

  TonalStraight(new double[] {0, 1}),
  TonalShift(new double[]    {1, 0});

  public static final double Threshold = 0.99;

  public final double[] flags;

  VlPeakType(double[] flags) { this.flags = flags; }

  public static VlPeakType fromRaw(double[] flags) {
    for (int k = 0; k < flags.length; k++) {
      flags[k] = flags[k] > Threshold ? 1 : 0;
    }
    if (flags[0] == 1) return TonalShift;
    else if (flags[1] == 1) return TonalStraight;
    return null;
  }
}

package io.vacco.volach.schema;

public enum VlToneType {
  STRAIGHT(new double[] {0, 1}),
  SHIFT(new double[] {1, 0});

  public final double[] flags;

  VlToneType(double[] flags) { this.flags = flags; }

  public static VlToneType fromRaw(double[] flags, double threshold) {
    for (int k = 0; k < flags.length; k++) {
      flags[k] = flags[k] > threshold ? 1 : 0;
    }
    if (flags[0] == 1) return SHIFT;
    else if (flags[1] == 1) return STRAIGHT;
    return null;
  }
}

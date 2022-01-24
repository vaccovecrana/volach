package io.vacco.volach.schema;

import java.io.Serializable;

public class VlMatchRange implements Serializable {

  private static final long serialVersionUID = VlConstants.schemaVersion;

  public long startSmp, endSmp;
  public int fpMatches;

  public long length() { return endSmp - startSmp; }

  public static VlMatchRange from(long startSmp, long endSmp, int fpMatches) {
    VlMatchRange r = new VlMatchRange();
    r.startSmp = startSmp;
    r.endSmp = endSmp;
    r.fpMatches = fpMatches;
    return r;
  }

  @Override public String toString() {
    return String.format(
        "Rng[t0: %012d, t1: %012d, len: %012d, fps: %04d]",
        startSmp, endSmp, length(), fpMatches
    );
  }
}

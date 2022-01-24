package io.vacco.volach.schema;

import java.io.Serializable;

public class VlFftPeak implements Serializable {

  private static final long serialVersionUID = VlConstants.schemaVersion;

  public int frq; // peak attributes somewhere near a detected tone feature.
  public long smp;
  public long fftOff;
  public float val;

  public long trackId = -1;

  public VlFftPeak withTrackId(long trackId) {
    this.trackId = trackId;
    return this;
  }

  public String sampleKey() {
    return String.format("%012d@%04d", smp, frq);
  }
  public String fftKey() { return String.format("%08d@%04d", fftOff, frq); }

  public double fftDistanceTo(VlFftPeak other) {
    double x0 = this.fftOff;
    double y0 = this.frq;
    double x1 = other.fftOff;
    double y1 = other.frq;
    return Math.hypot(x0 - x1, y0 - y1);
  }

  @Override public String toString() {
    return String.format("%s, %d", fftKey(), trackId);
  }
}

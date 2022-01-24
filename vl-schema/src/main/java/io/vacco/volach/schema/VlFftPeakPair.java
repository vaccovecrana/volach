package io.vacco.volach.schema;

import java.io.Serializable;
import java.util.Objects;

public class VlFftPeakPair implements Serializable {

  private static final long serialVersionUID = VlConstants.schemaVersion;

  public VlFftPeak pk0, pk1;
  public double fftDt;

  public String fftFreqId() {
    return String.format("%03d:%03d:%.8f", pk0.frq, pk1.frq, fftDt);
  }

  @Override public String toString() {
    return String.format("%s -> %s -> %s", pk0.fftKey(), pk1.fftKey(), fftFreqId());
  }

  public static VlFftPeakPair from(VlFftPeak pk0, VlFftPeak pk1) {
    VlFftPeakPair p = new VlFftPeakPair();
    p.pk0 = Objects.requireNonNull(pk0);
    p.pk1 = Objects.requireNonNull(pk1);
    p.fftDt = p.pk0.fftDistanceTo(p.pk1);
    return p;
  }

}

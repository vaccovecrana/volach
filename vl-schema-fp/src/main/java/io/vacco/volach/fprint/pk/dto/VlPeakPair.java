package io.vacco.volach.fprint.pk.dto;

import java.io.Serializable;

public class VlPeakPair implements Serializable {

  public static final long serialVersionUID = 0;

  public byte  f0, f1;
  public short hilbertDelta;
  public int   hilbertOffset;
  public int   sampleOffset;
  public int   trackId = -1;

  public static VlPeakPair from(VlAnchorPoint p0, VlAnchorPoint p1) {
    VlPeakPair p = new VlPeakPair();
    p.f0 = (byte) p0.yA();
    p.f1 = (byte) p1.yA();
    p.hilbertDelta = (short) (p1.xA() - p0.xA());
    p.hilbertOffset = p0.xA();
    p.sampleOffset = p0.smpOff;
    return p;
  }

  public String freqId() { return String.format("%03d%03d%04d", f0, f1, hilbertDelta); }
  public String timeId() { return String.format("%s-%08d", freqId(), hilbertOffset); }

  @Override
  public String toString() {
    return String.format(
        "pkh[f0: %03d, f1: %03d, hdt: %04d, hoff: %04d, trk: %d]",
        f0, f1, hilbertDelta, hilbertOffset, trackId
    );
  }
}

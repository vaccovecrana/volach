package io.vacco.volach.schema;

import java.io.Serializable;
import java.util.*;
import static io.vacco.volach.schema.VlArrays.*;

public class VlFftRegion implements Serializable {

  private static final long serialVersionUID = VlConstants.schemaVersion;

  public float[][] fftMtx;
  public final Map<Integer, Long> smpIdx = new TreeMap<>();
  public VlToneType toneType; // indicates a tone feature.
  public VlFftPeak peak;

  public static VlFftRegion from(int heightIdx, int height, VlFftSample ... samples) {
    float pkV;
    int pkIdx;
    VlFftRegion r = new VlFftRegion();
    r.fftMtx = new float[samples.length][];
    r.peak = new VlFftPeak();

    for (int k = 0; k < samples.length; k++) {
      r.fftMtx[k] = new float[height];
      r.smpIdx.put(k, samples[k].sampleOffset);
      System.arraycopy(samples[k].realQtr, heightIdx, r.fftMtx[k], 0, height);
      pkV = VlArrays.max(r.fftMtx[k], r.peak.val);
      if (pkV != r.peak.val) {
        pkIdx = VlArrays.indexOf(r.fftMtx[k], pkV);
        r.peak.val = pkV;
        r.peak.smp = samples[k].sampleOffset;
        r.peak.fftOff = samples[k].sampleFftOffset;
        r.peak.frq = heightIdx + pkIdx;
      }
    }
    return r;
  }

  @Override public int hashCode() {
    return Arrays.deepHashCode(fftMtx);
  }

  @Override public String toString() {
    return String.format(
        "VlFftRegion[%s, off: %08d, pkv: %.8f, tn: %s]",
        peak.sampleKey(), peak.fftOff, peak.val, toneType
    );
  }
}

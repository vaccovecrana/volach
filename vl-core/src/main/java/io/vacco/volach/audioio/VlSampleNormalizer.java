package io.vacco.volach.audioio;

import io.vacco.volach.wavelet.dto.VlAudioIOParameters;

public class VlSampleNormalizer {

  private final float[] v = new float[] {Float.MIN_VALUE, 0, 0}; // max, offset, temp

  // assumes data values come in as signed samples.
  public VlSampleNormalizer(VlAudioIOParameters p, float normalizationLimit) { // usually 0.95 (close to 0.0Db)
    VlSignalExtractor.from(p.src, p.analysisSampleSize, true, 0).forEach(chunk -> {
      for (int i = 0; i < chunk.data.capacity(); i++) {
        v[2] = Math.abs(chunk.data.get(i));
        v[0] = Math.max(v[2], v[0]);
      }
    });
    v[1] = normalizationLimit - v[0];
  }

  public float getOffset() { return v[1]; }
}

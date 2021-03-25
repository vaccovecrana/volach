package io.vacco.volach.fprint.pk.dto;

import io.vacco.volach.wavelet.dto.VlAnalysisSample;

public class VlAnalysisRegion {

  public static final int RegionSize = 32, CutoffFreqBands = 128;

  public VlAnalysisSample[] samples;
  public float[][] spectrum;
}

package io.vacco.volach.wavelet.dto;

import io.vacco.volach.wavelet.VlWpNode;
import io.vacco.volach.wavelet.type.VlWavelet;
import java.net.URL;

public class VlAnalysisParameters {

  public URL src;
  public boolean normalizeSamples;
  public int analysisSampleSize, level;
  public VlWavelet wavelet;
  public VlWpNode.Order order;

  public static VlAnalysisParameters from(URL src, int analysisSampleSize, int level, boolean normalize, VlWavelet wavelet, VlWpNode.Order order) {
    VlAnalysisParameters p = new VlAnalysisParameters();
    p.src = src;
    p.analysisSampleSize = analysisSampleSize;
    p.level = level;
    p.wavelet = wavelet;
    p.order = order;
    p.normalizeSamples = normalize;
    return p;
  }
}

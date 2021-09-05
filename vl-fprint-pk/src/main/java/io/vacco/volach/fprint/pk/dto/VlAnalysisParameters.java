package io.vacco.volach.fprint.pk.dto;

import io.vacco.jtinn.net.JtNetwork;
import io.vacco.volach.wavelet.VlWpNode;
import io.vacco.volach.wavelet.dto.VlAudioIOParameters;
import io.vacco.volach.wavelet.type.VlBattle23;

public class VlAnalysisParameters {

  public double peakMagnitudeThreshold, peakDistanceMin, peakDistanceMax;
  public int frequencyCutoff;

  public int netRegionSize;
  public int netRegionSizeHalf;
  public int netYSlide;

  public JtNetwork network;

  public static VlAnalysisParameters from(double peakMagnitudeThreshold,
                                          double peakDistanceMin,
                                          double peakDistanceMax,
                                          int frequencyCutoff,
                                          int netRegionSize,
                                          int netYSlide) {
    VlAnalysisParameters p = new VlAnalysisParameters();
    p.peakMagnitudeThreshold = peakMagnitudeThreshold;
    p.peakDistanceMin = peakDistanceMin;
    p.peakDistanceMax = peakDistanceMax;
    p.frequencyCutoff = frequencyCutoff;
    p.netRegionSize = netRegionSize;
    p.netRegionSizeHalf = netRegionSize / 2;
    p.netYSlide = netYSlide;
    return p;
  }

  private static final VlAnalysisParameters analysisParams = from(
      0, 40, 120,
      128,
      24, 4
  );

  public static final VlAudioIOParameters audioIoParams = VlAudioIOParameters.from(
      null, 262_144, 9, true,
      new VlBattle23(), VlWpNode.Order.Sequency
  );

  public static VlAnalysisParameters forReference() {
    analysisParams.peakMagnitudeThreshold = 0.08;
    return analysisParams;
  }

  public static VlAnalysisParameters forQuery() {
    analysisParams.peakMagnitudeThreshold = 0.05;
    return analysisParams;
  }
}

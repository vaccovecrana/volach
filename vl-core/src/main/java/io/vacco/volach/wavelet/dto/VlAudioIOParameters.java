package io.vacco.volach.wavelet.dto;

import io.vacco.volach.wavelet.VlWpNode;
import io.vacco.volach.wavelet.type.VlWavelet;
import java.net.URL;

public class VlAudioIOParameters {

  public URL src;
  public boolean scaleToUnit;
  public int analysisSampleSize, level;
  public float normalizationOffset;
  public VlWavelet wavelet;
  public VlWpNode.Order order;

  public static VlAudioIOParameters from(URL src, int analysisSampleSize, int level,
                                         boolean scaleToUnit, float normalizationOffset,
                                         VlWavelet wavelet, VlWpNode.Order order) {
    VlAudioIOParameters p = new VlAudioIOParameters();
    p.src = src;
    p.analysisSampleSize = analysisSampleSize;
    p.level = level;
    p.wavelet = wavelet;
    p.order = order;
    p.scaleToUnit = scaleToUnit;
    p.normalizationOffset = normalizationOffset;
    return p;
  }
}

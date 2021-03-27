package io.vacco.volach.fprint.pk.dto;

import io.vacco.volach.wavelet.dto.VlAnalysisChunk;
import java.util.ArrayList;
import java.util.List;

public class VlAnalysisRegion {

  public static final int RegionSize = 32, CutoffFreqBands = 128;
  public static final int HalfReg = RegionSize / 2;
  public static final int XMin = 10, XMax = 20, YSlide = 4;

  public VlAnalysisChunk chunk;
  public List<VlAnchorPoint> anchorPoints = new ArrayList<>();

  public float[][] spectrum;

}

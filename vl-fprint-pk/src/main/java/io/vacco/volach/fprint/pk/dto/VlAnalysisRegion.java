package io.vacco.volach.fprint.pk.dto;

import io.vacco.volach.wavelet.dto.VlAnalysisChunk;
import java.util.ArrayList;
import java.util.List;

public class VlAnalysisRegion {

  public VlAnalysisChunk chunk;
  public List<VlAnchorPoint> anchorPoints = new ArrayList<>();
  public float[][] spectrum;

}

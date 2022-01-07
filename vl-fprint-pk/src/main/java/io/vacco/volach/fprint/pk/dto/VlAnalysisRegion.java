package io.vacco.volach.fprint.pk.dto;

import io.vacco.volach.schema.fprint.VlAnchorPoint;
import io.vacco.volach.schema.wavelet.VlAnalysisChunk;
import java.util.ArrayList;
import java.util.List;

public class VlAnalysisRegion {

  public VlAnalysisChunk chunk;
  public List<VlAnchorPoint> anchorPoints = new ArrayList<>();
  public float[][] spectrum;

}

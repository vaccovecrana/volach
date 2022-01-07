package io.vacco.volach.schema.fprint;

import java.util.ArrayList;
import java.util.List;

public class VlTrainingDataSet {

  public static class VlTrainingSource {
    public String file;
    public List<VlAnchorPoint> anchors = new ArrayList<>();
  }

  public static class VlSampleSource {
    public String filePath;
    public float[][] spectrum;
    public float min, max;
  }

  public List<VlTrainingSource> sources;
}

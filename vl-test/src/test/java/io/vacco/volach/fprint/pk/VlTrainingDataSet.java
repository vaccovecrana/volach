package io.vacco.volach.fprint.pk;

import java.util.List;

public class VlTrainingDataSet {

  public static class VlAnchorPoint {
    public int x, y;
    public float[][] region;
    public VlPeakType type;
    public boolean valid;
    @Override public String toString() {
      return String.format("[x: %s, y: %s, t: %s]", x, y, type);
    }
  }

  public static class VlTrainingSource {
    public String file;
    public List<VlAnchorPoint> anchors;
  }

  public static class VlSampleSource {
    public String filePath;
    public float[][] spectrum;
    public float min, max;
  }

  public List<VlTrainingSource> sources;
}

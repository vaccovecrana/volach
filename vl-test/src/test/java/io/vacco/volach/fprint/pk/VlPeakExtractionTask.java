package io.vacco.volach.fprint.pk;

import java.io.File;

import static io.vacco.volach.VlSpecUtil.*;

// Extracts reference anchor points from sample analysis data
public class VlPeakExtractionTask {

  public static void main(String[] args) throws Exception {
    File f5 = new File(sources[0][1]);
    VlTrainingDataSet.VlSampleSource s5 = mapper.readValue(f5, VlTrainingDataSet.VlSampleSource.class);

    int x = 1371, y = 16;
    float[][] patch = new float[32][32];
    regionSquare(s5.spectrum, x, y, patch);
    print2d(patch);
    System.out.printf("%s, %s%n", s5.min, s5.max);
  }

}

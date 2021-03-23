package io.vacco.volach.fprint.pk;

import java.io.File;

import static io.vacco.volach.VlSpecUtil.*;

// Extracts reference anchor points from sample analysis data
public class VlPeakExtractionTask {

  public static void main(String[] args) throws Exception {
    File anchorsFile = new File("./vl-test/peak-training/anchors.json");
    VlTrainingDataSet ds = mapper.readValue(anchorsFile, VlTrainingDataSet.class);

    for (VlTrainingDataSet.VlTrainingSource tSrc : ds.sources) {
      File spectrumFile = new File(tSrc.file);
      System.out.printf("%nReading [%s] anchor values from [%s]%n", tSrc.anchors.size(), spectrumFile.getAbsolutePath());

      VlTrainingDataSet.VlSampleSource sSrc = mapper.readValue(spectrumFile, VlTrainingDataSet.VlSampleSource.class);
      for (VlTrainingDataSet.VlAnchorPoint anchor : tSrc.anchors) {
        float[][] region = new float[trainingRegionSize][trainingRegionSize];
        System.out.printf("Extracting anchor point: [%s, %s]%n", anchor.x, anchor.y);
        regionSquare(sSrc.spectrum, anchor.x, anchor.y, region);
        anchor.region = region;
      }
    }

    File trainData = new File("./vl-test/peak-training/peaks.json");
    mapper.writerWithDefaultPrettyPrinter().writeValue(trainData, ds);
  }

}

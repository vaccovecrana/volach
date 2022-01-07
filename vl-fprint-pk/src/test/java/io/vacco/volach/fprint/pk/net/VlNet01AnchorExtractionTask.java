package io.vacco.volach.fprint.pk.net;

import io.vacco.volach.schema.fprint.VlAnchorPoint;
import io.vacco.volach.schema.fprint.VlTrainingDataSet;

import java.io.File;

import static io.vacco.volach.VlAnalysisUtil.*;
import static io.vacco.volach.fprint.pk.VlFprintArrays.*;
import static io.vacco.volach.fprint.pk.dto.VlAnalysisParameters.*;

public class VlNet01AnchorExtractionTask { // Extracts reference anchor points from JSON sample analysis data

  public static void main(String[] args) {
    File anchorsFile = new File("./vl-fprint-pk/peak-training/anchors.json");
    VlTrainingDataSet ds = json.fromJson(VlTrainingDataSet.class, anchorsFile);
    int rs = forReference().netRegionSize;

    for (VlTrainingDataSet.VlTrainingSource tSrc : ds.sources) {
      File spectrumFile = new File(tSrc.file);
      System.out.printf("%nReading [%s] anchor values from [%s]%n", tSrc.anchors.size(), spectrumFile.getAbsolutePath());

      VlTrainingDataSet.VlSampleSource sSrc = json.fromJson(VlTrainingDataSet.VlSampleSource.class, spectrumFile);
      for (VlAnchorPoint anchor : tSrc.anchors) {
        float[][] region = new float[rs][rs];
        System.out.printf("Extracting anchor point: [%s, %s]%n", anchor.x, anchor.y);
        regionSquare(sSrc.spectrum, anchor.x, anchor.y, region);
        anchor.region = region;
      }
    }

    File trainData = new File("./vl-fprint-pk/peak-training/peaks.json");
    json.toJson(ds, trainData);

    System.out.printf("Peak region data written to [%s]", trainData.getAbsolutePath());
  }

}

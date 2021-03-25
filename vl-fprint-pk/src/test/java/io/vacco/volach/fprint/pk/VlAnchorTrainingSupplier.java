package io.vacco.volach.fprint.pk;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vacco.jtinn.net.JtPredictionSample;
import io.vacco.jtinn.net.JtPredictionSampleSupplier;
import io.vacco.volach.fprint.pk.dto.VlPeakType;
import io.vacco.volach.util.VlArrays;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class VlAnchorTrainingSupplier implements JtPredictionSampleSupplier {

  private final List<VlTrainingDataSet.VlAnchorPoint> points;
  private final JtPredictionSample[] samples;

  public VlAnchorTrainingSupplier(ObjectMapper m, File peaksJson) throws IOException {
    VlTrainingDataSet dataSet = m.readValue(peaksJson, VlTrainingDataSet.class);
    this.points = dataSet.sources.stream().flatMap(src -> src.anchors.stream()).collect(Collectors.toList());
    this.samples = new JtPredictionSample[points.size()];
    int squareSize = points.get(0).region.length;

    for (int i = 0; i < samples.length; i++) {
      samples[i] = JtPredictionSample.of(
          new double[squareSize * squareSize],
          new double[VlPeakType.values().length]
      );
    }
  }

  @Override public JtPredictionSample[] get() {
    Collections.shuffle(points);
    for (int i = 0; i < samples.length; i++) {
      JtPredictionSample smp = samples[i];
      VlTrainingDataSet.VlAnchorPoint p = points.get(i);
      VlArrays.flatten(p.region, smp.features);
      smp.labels = p.type.flags;
    }
    return samples;
  }
}

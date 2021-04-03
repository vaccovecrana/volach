package io.vacco.volach.fprint.pk.net;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vacco.jtinn.net.*;
import io.vacco.volach.fprint.pk.dto.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.volach.fprint.pk.VlFprintArrays.*;

public class VlAnchorTrainingSupplier implements JtPredictionSampleSupplier {

  private final List<VlAnchorPoint> points;
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
      VlAnchorPoint p = points.get(i);
      flatten(p.region, smp.features);
      smp.labels = p.type.flags;
    }
    return samples;
  }
}

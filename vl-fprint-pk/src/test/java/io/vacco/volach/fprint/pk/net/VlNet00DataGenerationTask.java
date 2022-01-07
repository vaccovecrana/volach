package io.vacco.volach.fprint.pk.net;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.fprint.pk.VlPeakRegionExtractor;
import io.vacco.volach.schema.fprint.VlTrainingDataSet;

import java.io.*;
import java.net.URL;
import java.util.*;

import static io.vacco.volach.fprint.pk.VlFprintSpecUtil.*;
import static io.vacco.volach.fprint.pk.dto.VlAnalysisParameters.*;
import static io.vacco.volach.VlAnalysisUtil.*;

public class VlNet00DataGenerationTask { // Generates JSON training data from input samples

  public static void main(String[] args) throws Exception {

    List<float[]> freqSamples = new ArrayList<>();

    for (String[] src : sources) {
      URL  srcUrl = VlNet00DataGenerationTask.class.getResource(src[0]);
      File trainingData = new File(src[1]);
      File analysisData = new File(src[2]);
      VlTrainingDataSet.VlSampleSource ts = new VlTrainingDataSet.VlSampleSource();

      if (!trainingData.exists()) {
        System.out.printf(">>> Generating training data from [%s]%n", srcUrl);
        audioIoParams.src = srcUrl;
        freqSamples.clear();

        VlUpdateListener listener = new VlUpdateListener();
        withWriter(analysisData, out -> {
          VlPeakRegionExtractor.from(audioIoParams, forReference()).forEach(region -> {
            System.out.printf("Extracting [%s] wavelet packet samples%n", region.chunk.samples.length);
            for (float[] buffer : region.spectrum) {
              listener.onData(buffer, out, true);
              float[] copy = new float[buffer.length];
              System.arraycopy(buffer, 0, copy, 0, buffer.length);
              freqSamples.add(copy);
            }
          });
          listener.done();

          ts.filePath = Objects.requireNonNull(srcUrl).toString();
          ts.min = listener.range[0];
          ts.max = listener.range[1];
          ts.spectrum = freqSamples.toArray(float[][]::new);

          json.toJson(ts, trainingData);
        });
      }
    }
  }

}

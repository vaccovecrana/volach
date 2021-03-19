package io.vacco.volach.fprint.pk;

import io.vacco.volach.VlSpecUtil;
import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static io.vacco.volach.VlSpecUtil.*;

public class VlPeakSamplingTask { // "Generates JSON training data from input samples"

  public static final String[] sources = {
      /*
      "/Users/jjzazuet/Desktop/sample-00.mp3",
      "/Users/jjzazuet/Desktop/sample-01.mp3",
      "/Users/jjzazuet/Desktop/sample-02.mp3",
      "/Users/jjzazuet/Desktop/sample-03.mp3",
      "/Users/jjzazuet/Desktop/sample-04.mp3",
      "/Users/jjzazuet/Desktop/sample-05.mp3",
       */
      src5
  };


  public static void main(String[] args) throws Exception {
    List<float[]> freqSamples = new ArrayList<>();

    for (String src : sources) {
      File f = new File(src);
      File trainingData = new File(format("./build/%s-spectrum.json", f.getName()));
      File analysisData = new File(format("./build/%s-spectrum.csv", f.getName()));

      VlUpdateListener listener = new VlUpdateListener();
      VlTrainingSource ts = new VlTrainingSource();
      VlAnalysisParameters trainParams = VlSpecUtil.trainingParams;

      if (!trainingData.exists()) {
        System.out.printf(">>> Generating training data from [%s]%n", f.getAbsolutePath());
        trainParams.src = f.toURI().toURL();
        freqSamples.clear();

        withWriter(analysisData, out -> {
          VlPeakAnalysisExtractor.from(trainParams, 128).forEach(chunk -> {
            System.out.printf("Extracting training data on [%s] wavelet packet samples%n", chunk.length);
            for (float[] buffer : chunk) {
              listener.onData(buffer, out, true);
              float[] copy = new float[buffer.length];
              System.arraycopy(buffer, 0, copy, 0, buffer.length);
              freqSamples.add(copy);
            }
          });
          listener.done();

          ts.filePath = f.getAbsolutePath();
          ts.min = listener.range[0];
          ts.max = listener.range[1];
          ts.spectrum = freqSamples.toArray(float[][]::new);

          try { mapper.writerWithDefaultPrettyPrinter().writeValue(trainingData, ts); }
          catch (IOException e) { throw new IllegalStateException(e); }
        });
      }
    }
  }

}

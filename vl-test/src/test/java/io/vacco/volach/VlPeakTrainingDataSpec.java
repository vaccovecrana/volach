package io.vacco.volach;

import io.vacco.volach.fprint.pk.VlPeakAnalysisExtractor;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static j8spec.J8Spec.*;
import static java.lang.String.format;
import static io.vacco.volach.VlSpecUtil.withWriter;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlPeakTrainingDataSpec {

  public static class VlTrainingSource {
    public String filePath;
    public float[][] spectrum;
    public float[] anchorPoints;
    public float min, max;
  }

  static {
    String[] sources = {
        /*
        "/Users/jjzazuet/Desktop/sample-00.mp3",
        "/Users/jjzazuet/Desktop/sample-01.mp3",
        "/Users/jjzazuet/Desktop/sample-02.mp3",
        "/Users/jjzazuet/Desktop/sample-03.mp3",
        "/Users/jjzazuet/Desktop/sample-04.mp3",
        "/Users/jjzazuet/Desktop/sample-05.mp3",
         */
        "/Users/jjzazuet/Desktop/sample-06.mp3"
    };

    it("Generates JSON training data from input samples", () -> {

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
                for (int i1 = 0; i1 < buffer.length; i1++) { buffer[i1] = Math.abs(buffer[i1]); }
                freqSamples.add(buffer);
                listener.onData(buffer, out, true);
              }
            });
            listener.done();

            ts.filePath = f.getAbsolutePath();
            ts.min = listener.range[0];
            ts.max = listener.range[1];
            ts.spectrum = freqSamples.toArray(float[][]::new);

            try { VlSpecUtil.mapper.writerWithDefaultPrettyPrinter().writeValue(trainingData, ts); }
            catch (IOException e) { throw new IllegalStateException(e); }
          });
        }
      }
    });

  }
}

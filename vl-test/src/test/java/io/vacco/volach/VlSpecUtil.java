package io.vacco.volach;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vacco.volach.util.VlArrays;
import io.vacco.volach.wavelet.VlWpNode;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.type.VlBattle23;
import io.vacco.volach.wavelet.type.VlHaar1;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.function.Consumer;

public class VlSpecUtil {

  public static final String
      src5 = "/Users/jjzazuet/Desktop/sample-05.mp3",
      src6 = "/Users/jjzazuet/Desktop/sample-06.mp3";

  public static final ObjectMapper mapper = new ObjectMapper();

  public static final VlAnalysisParameters analysisParams = VlAnalysisParameters.from(
      null, 65535, 10, true,
      new VlHaar1(), VlWpNode.Order.Sequency
  );

  public static final VlAnalysisParameters trainingParams = VlAnalysisParameters.from(
      null, 131_071, 9, false,
      new VlBattle23(), VlWpNode.Order.Sequency
  );

  public static FloatBuffer from(float[] values) {
    FloatBuffer b = VlArrays.floatBuffer(values.length);
    b.put(values);
    return b;
  }

  public static void withWriter(File output, Consumer<PrintWriter> writerConsumer) throws IOException {
    try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output)))) {
      writerConsumer.accept(out);
    }
  }

  public static void print2d(FloatBuffer[] in) {
    for (int i = 0; i < in.length; i++) {
      FloatBuffer data = in[i];
      for (int k = 0; k < data.capacity(); k++) {
        System.out.printf("[%s, %s, %s], %n", i, k, data.get(k));
      }
    }
  }

  public static void print2d(float[][] in) {
    for (int i = 0; i < in.length; i++) {
      float[] data = in[i];
      for (int k = 0; k < data.length; k++) {
        System.out.printf("[%s, %s, %s], %n", i, k, data[k]);
      }
    }
  }

}

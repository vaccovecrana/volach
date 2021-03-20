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

  public static final String[][] sources = {
      {"/Users/jjzazuet/Desktop/sample-001.mp3", "./vl-test/build/sample-001.mp3-spectrum.json", "./vl-test/build/sample-001.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-002.mp3", "./vl-test/build/sample-002.mp3-spectrum.json", "./vl-test/build/sample-002.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-003.mp3", "./vl-test/build/sample-003.mp3-spectrum.json", "./vl-test/build/sample-003.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-004.mp3", "./vl-test/build/sample-004.mp3-spectrum.json", "./vl-test/build/sample-004.mp3-spectrum.csv"}
  };

  public static final ObjectMapper mapper = new ObjectMapper();

  public static final VlAnalysisParameters analysisParams = VlAnalysisParameters.from(
      null, 65535, 10, true,
      new VlHaar1(), VlWpNode.Order.Sequency
  );

  public static final VlAnalysisParameters trainingParams = VlAnalysisParameters.from(
      null, 131_071, 9, true,
      new VlBattle23(), VlWpNode.Order.Sequency
  );

  public static FloatBuffer from(float[] values) {
    FloatBuffer b = VlArrays.floatBuffer(values.length);
    b.put(values);
    return b;
  }

  public static void withWriter(File output, Consumer<PrintWriter> writerConsumer) throws IOException {
    try (PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(output, false)))) {
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

  public static void regionSquare(float[][] in, int i, int j, float[][] out) {
    int size = out.length;
    int half = size / 2;
    int tl0 = i - half, tl1 = j - half;
    for (int k = 0; k < size; k++) {
      System.arraycopy(in[k + tl0], tl1, out[k], 0, size);
    }
  }

}

package io.vacco.volach;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.function.Consumer;

import static io.vacco.volach.audioio.VlArrays.floatBuffer;

public class VlSpecUtil {

  public static final ObjectMapper mapper = new ObjectMapper();

  public static void withWriter(File output, Consumer<PrintWriter> writerConsumer) throws FileNotFoundException {
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

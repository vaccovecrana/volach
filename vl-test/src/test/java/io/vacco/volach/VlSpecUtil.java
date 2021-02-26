package io.vacco.volach;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vacco.volach.util.VlArrays;

import java.io.*;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.function.Consumer;

public class VlSpecUtil {

  public static final ObjectMapper mapper = new ObjectMapper();

  public static FloatBuffer from(float[] values) {
    FloatBuffer b = VlArrays.floatBuffer(values.length);
    b.put(values);
    return b;
  }

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

  public static void writeCoefficients(File target, FloatBuffer[] coefficients) throws FileNotFoundException {
    float[] range = new float[2];
    float[][] buffer = new float[1][];

    withWriter(target, out -> {
      for (FloatBuffer floatBuffer : coefficients) {
        if (buffer[0] == null) {
          buffer[0] = new float[floatBuffer.capacity()];
        }
        floatBuffer.get(buffer[0]);
        out.println(Arrays.toString(buffer[0]).replace("[", "").replace("]", ""));
        for (float v : buffer[0]) {
          if (v < range[0]) range[0] = v;
          if (v > range[1]) range[1] = v;
        }
      }
    });

    System.out.printf("vmin=%s, vmax=%s%n", range[0] / 8, range[1] / 8);
  }
}

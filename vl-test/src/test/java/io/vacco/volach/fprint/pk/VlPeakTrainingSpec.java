package io.vacco.volach.fprint.pk;

import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;

import static io.vacco.volach.VlSpecUtil.*;
import static j8spec.J8Spec.*;
import static org.junit.Assert.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlPeakTrainingSpec {

  private static void regionSquare(float[][] in, int i, int j, float[][] out) {
    int size = out.length;
    int half = size / 2;
    int tl0 = i - half, tl1 = j - half;
    for (int k = 0; k < size; k++) {
      System.arraycopy(in[k + tl0], tl1, out[k], 0, size);
    }
  }

  static {

    float[][] example = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 7, 1, 1, 0, 0, 0},
        {0, 0, 1, 8, 1, 0, 0, 0},
        {0, 0, 1, 1, 9, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
    };

    float[][] expected = {
        {7, 1, 1},
        {1, 8, 1},
        {1, 1, 9}
    };

    it("Extracts a sub-region of a matrix", () -> {
      float[][] out = new float[3][3];
      regionSquare(example, 4, 3, out);
      assertEquals(out[0][0], expected[0][0], 0.0);
      assertEquals(out[2][2], expected[2][2], 0.0);
    });

    it("Extracts training data from reference anchor points", () -> {
      File f5 = new File("./build/sample-05.mp3-spectrum.json");
      VlTrainingSource s5 = mapper.readValue(f5, VlTrainingSource.class);

      int x = 878, y = 23;
      float[][] patch = new float[32][32];
      regionSquare(s5.spectrum, x, y, patch);
      print2d(patch);
      System.out.printf("%s, %s%n", s5.min, s5.max);
    });
  }

}

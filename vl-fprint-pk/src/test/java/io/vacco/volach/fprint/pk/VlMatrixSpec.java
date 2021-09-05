package io.vacco.volach.fprint.pk;

import io.vacco.volach.fprint.pk.dto.VlAnchorPoint;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.util.Arrays;
import static io.vacco.volach.fprint.pk.VlFprintArrays.*;
import static j8spec.J8Spec.*;
import static org.junit.Assert.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlMatrixSpec {

  public static int[] indexAt(float[][] region, int x0, int y0, float[][] global) {
    return new int[2];
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

    double[] expectedFlat = {7, 1, 1, 1, 8, 1, 1, 1, 9};

    float[][] offsetNw = {
        {1, 9, 1, 0, 0},
        {1, 1, 1, 0, 0},
        {1, 1, 1, 0, 0},
        {1, 1, 1, 0, 0},
        {1, 1, 1, 0, 0},
    };

    float[][] offsetSe = {
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 9},
    };

    it("Extracts a sub-region of a matrix", () -> {
      float[][] out = new float[3][3];
      regionSquare(example, 4, 3, out);
      assertEquals(out[0][0], expected[0][0], 0.0);
      assertEquals(out[2][2], expected[2][2], 0.0);
    });

    it("Finds a local peak, indicating it's absolute offset", () -> {
      System.out.println(VlAnchorPoint.maxLocal(expected, 0)); // TODO add matrix processing constraint annotations (i.e. @AssumesMatrixIsSquare, 90 deg rot, etc).
      System.out.println(VlAnchorPoint.maxLocal(offsetNw, 0));
      System.out.println(VlAnchorPoint.maxLocal(offsetSe, 0));
    });

    it("Can flatten a matrix array", () -> {
      double[] flat = new double[9];
      flatten(expected, flat);
      for (int i = 0; i < flat.length; i++) {
        assertEquals(expectedFlat[i], flat[i], 0);
      }
      System.out.println(Arrays.toString(flat));
    });
  }

}

package io.vacco.volach.fprint.pk;

import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import static io.vacco.volach.VlSpecUtil.*;
import static j8spec.J8Spec.*;
import static org.junit.Assert.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlPeakTrainingSpec {

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
/*
    it("It performs peak training from sample dataset", () -> {
      VlTrainingDataSet ds = mapper.readValue(
          // TODO load this from a File instead.
          VlPeakTrainingSpec.class.getResource("/peak-training/peak-training.json"), VlTrainingDataSet.class
      );
    });
*/
  }
}

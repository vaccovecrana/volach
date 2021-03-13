package io.vacco.volach;

import io.vacco.volach.util.VlArrays;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.util.Arrays;

import static org.junit.Assert.*;
import static j8spec.J8Spec.*;

@RunWith(J8SpecRunner.class)
public class VlBitsSpec {

  static {
    it("Can write two integer numbers into a double array", () -> {
      int v0 = 761776340, v1 = 393216;
      double[] v0d = new double[32], v1d = new double[32];
      double[] out = new double[64];
      VlArrays.packPair(v0, v1, v0d, v1d, out);
      System.out.printf("%s -> %s%n", v0, Arrays.toString(v0d));
      System.out.printf("%s -> %s%n", v1, Arrays.toString(v1d));
      System.out.println(Arrays.toString(out));

      int[] iOut = new int[2];
      VlArrays.unpackPair(out, v0d, v1d, iOut);
      System.out.println(Arrays.toString(iOut));

      assertEquals(iOut[0], v0);
      assertEquals(iOut[1], v1);
    });
  }

}

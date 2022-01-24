package io.vacco.volach.impl;

import io.vacco.volach.schema.VlFftSample;
import java.util.Arrays;

public class VlFft {

  private final float[] iReal;
  private final float[] iImg;

  private final float[] xReal, xRealQtr;
  private final float[] xImg;

  private final boolean direct;
  public final int bufferSize;

  public VlFft(int bufferSize, boolean direct) {
    this.bufferSize = bufferSize;
    this.iReal = new float[bufferSize];
    this.xReal = new float[bufferSize];
    this.xRealQtr = new float[bufferSize / 4];
    this.xImg  = new float[bufferSize];
    this.iImg  = new float[bufferSize];
    this.direct = direct;
  }

  private int bitReverseReference(int j, int nu) {
    int j2;
    int j1 = j;
    int k = 0;
    for (int i = 1; i <= nu; i++) {
      j2 = j1 / 2;
      k = 2 * k + j1 - 2 * j2;
      j1 = j2;
    }
    return k;
  }

  private VlFftSample apply(float[] iR, float[] iI) {
    int n = iR.length;
    double ld = Math.log(n) / Math.log(2.0);

    if (((int) ld) - ld != 0) {
      throw new IllegalArgumentException("The number of elements is not a power of 2.");
    }

    int nu = (int) ld;
    int n2 = n / 2;
    int nu1 = nu - 1;

    float tReal, tImag, p, arg, c, s;
    float constant = (float) (direct ? -2 * Math.PI : 2 * Math.PI);

    for (int i = 0; i < n; i++) {
      xReal[i] = iR[i];
      xImg[i] = iI[i];
    }

    // First phase - calculation
    int k = 0;
    for (int l = 1; l <= nu; l++) {
      while (k < n) {
        for (int i = 1; i <= n2; i++) {
          p = bitReverseReference(k >> nu1, nu);
          arg = constant * p / n;
          c = (float) Math.cos(arg);
          s = (float) Math.sin(arg);
          tReal = xReal[k + n2] * c + xImg[k + n2] * s;
          tImag = xImg[k + n2] * c - xReal[k + n2] * s;
          xReal[k + n2] = xReal[k] - tReal;
          xImg[k + n2] = xImg[k] - tImag;
          xReal[k] += tReal;
          xImg[k] += tImag;
          k++;
        }
        k += n2;
      }
      k = 0;
      nu1--;
      n2 /= 2;
    }

    // Second phase - recombination
    k = 0;
    int r;
    while (k < n) {
      r = bitReverseReference(k, nu);
      if (r > k) {
        tReal = xReal[k];
        tImag = xImg[k];
        xReal[k] = xReal[r];
        xImg[k] = xImg[r];
        xReal[r] = tReal;
        xImg[r] = tImag;
      }
      k++;
    }

    System.arraycopy(xReal, 0, xRealQtr, 0, xRealQtr.length);
    return VlFftSample.from(Arrays.hashCode(iR), xReal, xRealQtr, xImg);
  }

  public VlFftSample apply(float[] inputReal) {
    if (inputReal.length != iReal.length) {
      String msg = String.format("Invalid buffer size: %s", inputReal.length);
      throw new IllegalArgumentException(msg);
    }
    System.arraycopy(inputReal, 0, iReal, 0, inputReal.length);
    return apply(iReal, iImg);
  }

}

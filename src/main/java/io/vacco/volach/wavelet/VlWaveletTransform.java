package io.vacco.volach.wavelet;

import static java.lang.String.format;

public class VlWaveletTransform {

  public static boolean isBinary(int number) {
    boolean isBinary = false;
    int power = (int) (Math.log(number) / Math.log(2));
    double result = Math.pow(2, power);
    if (result == number)
      isBinary = true;
    return isBinary;
  }

  public static int getExponent(double f) {
    return (int) (Math.log(f) / Math.log(2));
  }

  protected static int calcExponent(int number) {
    if (!isBinary(number)) {
      throw new IllegalArgumentException(format("[%s] is not binary", number));
    }
    return getExponent(number);
  }

  /**
   * Performs a 1-D forward transform from time domain to Hilbert domain.
   */
  public static float[] forward(float[] arrTime, int level, VlWavelet wavelet) {
    if (!isBinary(arrTime.length)) {
      throw new IllegalArgumentException(format("Array of length [%s] is not 2^p", arrTime.length));
    }
    if (level < 0 || level > calcExponent(arrTime.length)) {
      throw new IllegalArgumentException(format("Level [%s] is out of range for given array", level));
    }

    float[] arrHilb = new float[arrTime.length];
    System.arraycopy(arrTime, 0, arrHilb, 0, arrTime.length);

    int k = arrTime.length;
    int h = arrTime.length;
    int transformWavelength = wavelet.transformWavelength;
    int l = 0;

    while (h >= transformWavelength && l < level) {
      int g = k / h;
      for (int p = 0; p < g; p++) {
        float[] iBuf = new float[h];
        for (int i = 0; i < h; i++) {
          iBuf[i] = arrHilb[i + (p * h)];
        }
        float[] oBuf = wavelet.forward(iBuf, h);
        for (int i = 0; i < h; i++) {
          arrHilb[i + (p * h)] = oBuf[i];
        }
      }
      h = h >> 1;
      l++;
    }
    return arrHilb;
  }
}

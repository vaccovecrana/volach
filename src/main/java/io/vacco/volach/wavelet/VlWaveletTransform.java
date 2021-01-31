package io.vacco.volach.wavelet;

import java.nio.FloatBuffer;
import static java.lang.String.format;
import static io.vacco.volach.audioio.VlArrays.*;

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
  public static FloatBuffer forward(FloatBuffer arrTime, int level, VlWavelet wavelet) {
    int length = arrTime.capacity();
    if (!isBinary(length)) {
      throw new IllegalArgumentException(format("Array of length [%s] is not 2^p", length));
    }
    if (level < 0 || level > calcExponent(length)) {
      throw new IllegalArgumentException(format("Level [%s] is out of range for given array", level));
    }

    FloatBuffer arrHilb = floatBuffer(length);
    copy(arrTime, 0, arrHilb, 0, length);

    int k = length;
    int h = length;
    int l = 0;

    while (h >= wavelet.transformWavelength && l < level) {
      int g = k / h;
      for (int p = 0; p < g; p++) {
        FloatBuffer iBuf = floatBuffer(h);
        copy(arrHilb, p * h, iBuf, 0, h);
        FloatBuffer oBuf = wavelet.forward(iBuf, h);
        copy(oBuf, 0, arrHilb, p * h, h);
      }
      h = h >> 1;
      l++;
    }
    return arrHilb;
  }
}

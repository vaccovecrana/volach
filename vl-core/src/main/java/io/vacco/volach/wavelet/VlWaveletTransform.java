package io.vacco.volach.wavelet;

import io.vacco.volach.util.VlException;
import io.vacco.volach.wavelet.type.VlWavelet;
import static io.vacco.volach.util.VlMath.*;

public class VlWaveletTransform {

  /** Performs a 1-D forward transform from time domain to Hilbert domain. */
  public static float[] forward(float[] arrTime, int level, VlWavelet wavelet) {
    int length = arrTime.length;
    if (!isBinary(length)) {
      throw new VlException.VlNonPowerOfTwoException(length);
    }
    int exponent = calcExponent(length);
    if (level < 0 || level > calcExponent(length)) {
      throw new VlException.VlLevelOutOfRangeException(level, length, exponent);
    }

    float[] arrHilb = new float[length];

    System.arraycopy(arrTime, 0, arrHilb, 0, length);

    int k = length;
    int h = length;
    int l = 0;

    while (h >= wavelet.transformWavelength && l < level) {
      int g = k / h;
      for (int p = 0; p < g; p++) {
        float[] iBuf = new float[h];
        System.arraycopy(arrHilb, p * h, iBuf, 0, h);
        float[] oBuf = wavelet.forward(iBuf, h);
        System.arraycopy(oBuf, 0, arrHilb, p * h, h);
      }
      h = h >> 1;
      l++;
    }
    return arrHilb;
  }

  /**
   * Given an input signal and a wavelet, determine if a target analysis level is defined.
   *
   * @param sampleSize the input signal size
   * @param level the desired analysis level
   * @param wavelet the target analysis wavelet
   *
   * @throws io.vacco.volach.util.VlException.VlUndefinedDecompositionLevelException
   *  if the sample size/level/wavelet combination is invalid.
   */
  public static void validate(int sampleSize, int level, VlWavelet wavelet) {
    if (!isBinary(sampleSize)) {
      throw new VlException.VlNonPowerOfTwoException(sampleSize);
    }
    int test = sampleSize;
    for (int i = 0; i < level; i++) {
      test = test >> 1;
    }
    if (test < wavelet.motherWavelength) {
      throw new VlException.VlUndefinedDecompositionLevelException(level, sampleSize, wavelet);
    }
  }
}

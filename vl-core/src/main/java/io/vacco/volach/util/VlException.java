package io.vacco.volach.util;

import io.vacco.volach.wavelet.VlWavelet;

import static java.lang.String.format;

public class VlException {

  @SuppressWarnings("serial")
  public static class VlNonPowerOfTwoException extends RuntimeException {
    public final int size;
    public VlNonPowerOfTwoException(int size) {
      super(format("Size [%s] is not 2^p", size));
      this.size = size;
    }
  }

  @SuppressWarnings("serial")
  public static class VlLevelOutOfRangeException extends RuntimeException {
    public final int level, length, exponent;
    public VlLevelOutOfRangeException(int level, int length, int exponent) {
      super(format("Level [%s] is out of range for given exponent [%s] and array length [%s]", level, length, exponent));
      this.level = level;
      this.length = length;
      this.exponent = exponent;
    }
  }

  @SuppressWarnings("serial")
  public static class VlUndefinedDecompositionLevelException extends RuntimeException {
    public final int level, length;
    public final VlWavelet wavelet;
    public VlUndefinedDecompositionLevelException(int level, int length, VlWavelet wavelet) {
      super(format(
          "Decomposition level [%s] for sample length [%s], wavelet [%s] is undefined. More sample data needed.",
          level, length, wavelet
      ));
      this.level = level;
      this.length = length;
      this.wavelet = wavelet;
    }
  }
}

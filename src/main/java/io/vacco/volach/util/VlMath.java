package io.vacco.volach.util;

import static java.lang.String.format;

public class VlMath {

  public static boolean isBinary(int number) {
    boolean isBinary = false;
    int power = (int) (Math.log(number) / Math.log(2));
    double result = Math.pow(2, power);
    if (result == number) { isBinary = true; }
    return isBinary;
  }

  public static int getExponent(double f) {
    return (int) (Math.log(f) / Math.log(2));
  }

  public static int calcExponent(int number) {
    if (!isBinary(number)) {
      throw new IllegalArgumentException(format("[%s] is not binary", number));
    }
    return getExponent(number);
  }

  public static int nextPow2(int x) {
    return x == 1 ? 1 : Integer.highestOneBit(x - 1) * 2;
  }

}

package io.vacco.volach.dsp;

public class Fft {

  private final float[] window;

  public Fft(int sampleSize) {
    this.window = HammingWindow.generateCurve(sampleSize);
  }

  public FftSampleF fft(float[] inputReal, float[] inputImag, boolean direct) {
    int n = inputReal.length;
    double ld = Math.log(n) / Math.log(2.0);

    if (((int) ld) - ld != 0) {
      throw new IllegalArgumentException("The number of elements is not a power of 2.");
    }

    int nu = (int) ld;
    int n2 = n / 2;
    int nu1 = nu - 1;
    float[] xReal = new float[n];
    float[] xImag = new float[n];
    float tReal, tImag, p, arg, c, s;
    float constant = (float) (direct ? -2 * Math.PI : 2 * Math.PI);

    for (int i = 0; i < n; i++) {
      xReal[i] = inputReal[i];
      xImag[i] = inputImag[i];
    }

    // First phase - calculation
    int k = 0;
    for (int l = 1; l <= nu; l++) {
      while (k < n) {
        for (int i = 1; i <= n2; i++) {
          p = bitreverseReference(k >> nu1, nu);
          arg = constant * p / n;
          c = (float) Math.cos(arg);
          s = (float) Math.sin(arg);
          tReal = xReal[k + n2] * c + xImag[k + n2] * s;
          tImag = xImag[k + n2] * c - xReal[k + n2] * s;
          xReal[k + n2] = xReal[k] - tReal;
          xImag[k + n2] = xImag[k] - tImag;
          xReal[k] += tReal;
          xImag[k] += tImag;
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
      r = bitreverseReference(k, nu);
      if (r > k) {
        tReal = xReal[k];
        tImag = xImag[k];
        xReal[k] = xReal[r];
        xImag[k] = xImag[r];
        xReal[r] = tReal;
        xImag[r] = tImag;
      }
      k++;
    }
    return FftSampleF.from(xReal, xImag);
  }

  public FftSampleF fft(float[] inputReal, boolean direct) {
    if (inputReal.length != window.length) {
      throw new IllegalArgumentException(String.format("Invalid sample size: %s", inputReal.length));
    }
    float[] windowed = new float[inputReal.length];
    for (int i = 0; i < inputReal.length; i++) {
      windowed[i] = inputReal[i] * this.window[i];
    }
    return fft(windowed, new float[inputReal.length], direct);
  }

  private int bitreverseReference(int j, int nu) {
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
}

package io.vacco.volach.wavelet.type;

import java.nio.FloatBuffer;
import java.util.Arrays;

import static io.vacco.volach.util.VlArrays.*;

public abstract class VlWavelet {

  /** Wavelength of the mother wavelet and its matching scaling function. */
  public int motherWavelength;

  /** Minimal wavelength of a signal that can be transformed. */
  public int transformWavelength;

  /** The coefficients of the mother wavelet scaling (low pass filter) for decomposition. */
  public float[] scalingDeCom;

  /** The coefficients of the mother wavelet (high pass filter) for decomposition. */
  public float[] waveletDeCom;

  /** Perform forward transform from time domain to Hilbert domain. */
  public FloatBuffer forward(FloatBuffer arrTime, int arrTimeLength) {
    FloatBuffer arrHilb = floatBuffer(arrTimeLength);
    int hp = arrHilb.capacity() >> 1;
    for (int lp = 0; lp < hp; lp++) {
      arrHilb.put(lp + hp, 0);
      arrHilb.put(lp, 0);
      for (int j = 0; j < motherWavelength; j++) {
        int k = (lp << 1) + j;
        while (k >= arrHilb.capacity()) {
          k -= arrHilb.capacity();
        }
        float lowPass = arrHilb.get(lp) + arrTime.get(k) * scalingDeCom[j]; // low pass filter energy approximation
        float highPass = arrHilb.get(lp + hp) + arrTime.get(k) * waveletDeCom[j]; // high pass filter for detail
        arrHilb.put(lp, lowPass);
        arrHilb.put(lp + hp, highPass);
      }
    }
    return arrHilb;
  }

  /**
   * Build the matching coefficients for the wavelet (high pass) for decomposition
   * form the scaling (low pass) coefficients for decomposition of a filter.
   */
  protected void buildOrthonormalSpace() {
    waveletDeCom = new float[motherWavelength];
    for (int i = 0; i < motherWavelength; i++)
      if (i % 2 == 0) {
        waveletDeCom[i] = scalingDeCom[(motherWavelength - 1) - i];
      } else {
        waveletDeCom[i] = -scalingDeCom[(motherWavelength - 1) - i];
      }
  }

  @Override public String toString() { return getClass().getSimpleName(); }
}

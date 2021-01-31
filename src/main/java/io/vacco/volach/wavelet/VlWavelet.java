package io.vacco.volach.wavelet;

import java.nio.FloatBuffer;
import static io.vacco.volach.audioio.VlArrays.*;

public abstract class VlWavelet {

  /**
   * Wavelength of the mother wavelet and its matching scaling function.
   */
  public int motherWavelength;

  /**
   * Minimal wavelength of a signal that can be transformed.
   */
  public int transformWavelength;

  /**
   * The coefficients of the mother wavelet scaling (low pass filter) for decomposition.
   */
  public float[] scalingDeCom;

  /**
   * The coefficients of the mother wavelet (high pass filter) for decomposition.
   */
  public float[] waveletDeCom;

  /**
   * Perform forward transform from time domain to Hilbert domain.
   */
  public FloatBuffer forward(FloatBuffer arrTime, int arrTimeLength) {
    FloatBuffer arrHilb = floatBuffer(arrTimeLength);
    int h = arrHilb.capacity() >> 1;
    for (int i = 0; i < h; i++) {
      arrHilb.put(i + h, 0);
      arrHilb.put(i, 0);
      for (int j = 0; j < motherWavelength; j++) {
        int k = (i << 1) + j;
        while (k >= arrHilb.capacity()) {
          k -= arrHilb.capacity();
        }
        float lowPass = arrHilb.get(i) + arrTime.get(k) * scalingDeCom[j]; // low pass filter energy approximation
        float highPass = arrHilb.get(i + h) + arrTime.get(k) * waveletDeCom[j]; // high pass filter for detail
        arrHilb.put(i, lowPass);
        arrHilb.put(i + h, highPass);
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
}

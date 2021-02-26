package io.vacco.volach.wavelet.type;

/**
 * Alfred Haar's orthonormal wavelet transform.
 */
public class VlHaar1 extends VlWavelet {

  public VlHaar1() {
    double sqrt2 = Math.sqrt(2);
    this.transformWavelength = 2;
    this.motherWavelength = 2;
    this.scalingDeCom = new float[motherWavelength];
    scalingDeCom[0] = (float) (1 / sqrt2);
    scalingDeCom[1] = (float) (1 / sqrt2);
    this.waveletDeCom = new float[motherWavelength];
    waveletDeCom[0] = scalingDeCom[1];
    waveletDeCom[1] = -scalingDeCom[0];
  }
}

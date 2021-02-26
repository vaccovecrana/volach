package io.vacco.volach.wavelet.type;

/**
 * Ingrid Daubechies' orthonormal wavelet of eight coefficients and the scales;
 * normed, due to ||*||2 - euclidean norm.
 */
public class VlDaubechies4 extends VlWavelet {

  public VlDaubechies4() {
    this.transformWavelength = 2;
    this.motherWavelength = 8;

    this.scalingDeCom = new float[motherWavelength];
    scalingDeCom[0] = -0.010597401784997278f;
    scalingDeCom[1] = 0.032883011666982945f;
    scalingDeCom[2] = 0.030841381835986965f;
    scalingDeCom[3] = -0.18703481171888114f;
    scalingDeCom[4] = -0.02798376941698385f;
    scalingDeCom[5] = 0.6308807679295904f;
    scalingDeCom[6] = 0.7148465705525415f;
    scalingDeCom[7] = 0.23037781330885523f;

    buildOrthonormalSpace();
  }
}

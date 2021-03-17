package io.vacco.volach.wavelet.type;

public class VlDaubechies2 extends VlWavelet {

  public VlDaubechies2() {
    this.transformWavelength = 2;
    this.motherWavelength = 4;

    double sqrt3 = Math.sqrt(3);
    this.scalingDeCom = new float[this.motherWavelength];
    scalingDeCom[0] = (float) ((1. + sqrt3) / 4.);
    scalingDeCom[1] = (float) ((3. + sqrt3) / 4.);
    scalingDeCom[2] = (float) ((3. - sqrt3) / 4.);
    scalingDeCom[3] = (float) ((1. - sqrt3) / 4.);

    double sqrt02 = Math.sqrt(2);
    for (int i = 0; i < motherWavelength; i++) {
      scalingDeCom[i] /= sqrt02;
    }

    buildOrthonormalSpace();
  }

}

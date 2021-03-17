package io.vacco.volach.wavelet.type;

public class VlBattle23 extends VlWavelet {

  public VlBattle23() {
    this.transformWavelength = 8;
    this.motherWavelength = 23;

    this.scalingDeCom = new float[this.motherWavelength];
    scalingDeCom[0] = -0.002f;
    scalingDeCom[1] = -0.003f;
    scalingDeCom[2] = 0.006f;
    scalingDeCom[3] = 0.006f;
    scalingDeCom[4] = -0.013f;
    scalingDeCom[5] = -0.012f;
    scalingDeCom[6] = 0.030f;
    scalingDeCom[7] = 0.023f;
    scalingDeCom[8] = -0.078f;
    scalingDeCom[9] = -0.035f;
    scalingDeCom[10] = 0.307f;
    scalingDeCom[11] = 0.542f;
    scalingDeCom[12] = 0.307f;
    scalingDeCom[13] = -0.035f;
    scalingDeCom[14] = -0.078f;
    scalingDeCom[15] = 0.023f;
    scalingDeCom[16] = 0.030f;
    scalingDeCom[17] = -0.012f;
    scalingDeCom[18] = -0.013f;
    scalingDeCom[19] = 0.006f;
    scalingDeCom[20] = 0.006f;
    scalingDeCom[21] = -0.003f;
    scalingDeCom[22] = -0.002f;

    waveletDeCom = new float[motherWavelength];
    for (int i = 0; i < motherWavelength; i++)
      if (i % 2 == 0) {
        waveletDeCom[i] = scalingDeCom[(motherWavelength - 1) - i];
      } else {
        waveletDeCom[i] = -scalingDeCom[(motherWavelength - 1) - i];
      }
  }
}

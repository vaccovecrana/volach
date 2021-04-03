package io.vacco.volach.wavelet.type;

public class VlBiOrthogonal22 extends VlWavelet {

  public VlBiOrthogonal22() {
    this.transformWavelength = 2;
    this.motherWavelength = 6;

    this.scalingDeCom = new float[ motherWavelength ];
    scalingDeCom[ 0 ] = 0;
    scalingDeCom[ 1 ] = -0.1767766952966369f;
    scalingDeCom[ 2 ] = 0.3535533905932738f;
    scalingDeCom[ 3 ] = 1.0606601717798214f;
    scalingDeCom[ 4 ] = 0.3535533905932738f;
    scalingDeCom[ 5 ] = -0.1767766952966369f;

    this.waveletDeCom = new float[ motherWavelength ];
    waveletDeCom[ 0 ] = 0;
    waveletDeCom[ 1 ] = 0.3535533905932738f;
    waveletDeCom[ 2 ] = -0.7071067811865476f;
    waveletDeCom[ 3 ] = 0.3535533905932738f;
    waveletDeCom[ 4 ] = 0;
    waveletDeCom[ 5 ] = 0;
  }

}

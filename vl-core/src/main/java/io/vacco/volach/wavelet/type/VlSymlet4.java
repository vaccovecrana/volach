package io.vacco.volach.wavelet.type;

public class VlSymlet4 extends VlWavelet {

  public VlSymlet4() {
    this.transformWavelength = 2;
    this.motherWavelength = 8;

    this.scalingDeCom = new float[this.motherWavelength];
    scalingDeCom[ 0 ] = -0.07576571478927333f;
    scalingDeCom[ 1 ] = -0.02963552764599851f;
    scalingDeCom[ 2 ] = 0.49761866763201545f;
    scalingDeCom[ 3 ] = 0.8037387518059161f;
    scalingDeCom[ 4 ] = 0.29785779560527736f;
    scalingDeCom[ 5 ] = -0.09921954357684722f;
    scalingDeCom[ 6 ] = -0.012603967262037833f;
    scalingDeCom[ 7 ] = 0.0322231006040427f;

    buildOrthonormalSpace();
  }
}

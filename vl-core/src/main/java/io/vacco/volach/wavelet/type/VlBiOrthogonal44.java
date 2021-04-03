package io.vacco.volach.wavelet.type;

public class VlBiOrthogonal44 extends VlWavelet {

  public VlBiOrthogonal44() {
    this.transformWavelength = 2;
    this.motherWavelength = 10;

    this.scalingDeCom = new float[ motherWavelength ];
    this.scalingDeCom[ 0 ] = 0;
    this.scalingDeCom[ 1 ] = 0.03782845550726404f;
    this.scalingDeCom[ 2 ] = -0.023849465019556843f;
    this.scalingDeCom[ 3 ] = -0.11062440441843718f;
    this.scalingDeCom[ 4 ] = 0.37740285561283066f;
    this.scalingDeCom[ 5 ] = 0.8526986790088938f;
    this.scalingDeCom[ 6 ] = 0.37740285561283066f;
    this.scalingDeCom[ 7 ] = -0.11062440441843718f;
    this.scalingDeCom[ 8 ] = -0.023849465019556843f;
    this.scalingDeCom[ 9 ] = 0.03782845550726404f;

    this.waveletDeCom = new float[ motherWavelength ];
    this.waveletDeCom[ 0 ] = 0;
    this.waveletDeCom[ 1 ] = -0.06453888262869706f;
    this.waveletDeCom[ 2 ] = 0.04068941760916406f;
    this.waveletDeCom[ 3 ] = 0.41809227322161724f;
    this.waveletDeCom[ 4 ] = -0.7884856164055829f;
    this.waveletDeCom[ 5 ] = 0.41809227322161724f;
    this.waveletDeCom[ 6 ] = 0.04068941760916406f;
    this.waveletDeCom[ 7 ] = -0.06453888262869706f;
    this.waveletDeCom[ 8 ] = 0;
    this.waveletDeCom[ 9 ] = 0;

    // buildOrthonormalSpace( );
  }

}

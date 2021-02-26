package io.vacco.volach.wavelet.type;

public class VlDaubechies16 extends VlWavelet {

  public VlDaubechies16() {
    this.transformWavelength = 2;
    this.motherWavelength = 32;

    this.scalingDeCom = new float[this.motherWavelength];

    scalingDeCom[ 0 ] = -2.1093396300980412e-08f;
    scalingDeCom[ 1 ] = 2.3087840868545578e-07f;
    scalingDeCom[ 2 ] = -7.363656785441815e-07f;
    scalingDeCom[ 3 ] = -1.0435713423102517e-06f;
    scalingDeCom[ 4 ] = 1.133660866126152e-05f;
    scalingDeCom[ 5 ] = -1.394566898819319e-05f;
    scalingDeCom[ 6 ] = -6.103596621404321e-05f;
    scalingDeCom[ 7 ] = 0.00017478724522506327f;
    scalingDeCom[ 8 ] = 0.00011424152003843815f;
    scalingDeCom[ 9 ] = -0.0009410217493585433f;
    scalingDeCom[ 10 ] = 0.00040789698084934395f;
    scalingDeCom[ 11 ] = 0.00312802338120381f;
    scalingDeCom[ 12 ] = -0.0036442796214883506f;
    scalingDeCom[ 13 ] = -0.006990014563390751f;
    scalingDeCom[ 14 ] = 0.013993768859843242f;
    scalingDeCom[ 15 ] = 0.010297659641009963f;
    scalingDeCom[ 16 ] = -0.036888397691556774f;
    scalingDeCom[ 17 ] = -0.007588974368642594f;
    scalingDeCom[ 18 ] = 0.07592423604445779f;
    scalingDeCom[ 19 ] = -0.006239722752156254f;
    scalingDeCom[ 20 ] = -0.13238830556335474f;
    scalingDeCom[ 21 ] = 0.027340263752899923f;
    scalingDeCom[ 22 ] = 0.21119069394696974f;
    scalingDeCom[ 23 ] = -0.02791820813292813f;
    scalingDeCom[ 24 ] = -0.3270633105274758f;
    scalingDeCom[ 25 ] = -0.08975108940236352f;
    scalingDeCom[ 26 ] = 0.44029025688580486f;
    scalingDeCom[ 27 ] = 0.6373563320829833f;
    scalingDeCom[ 28 ] = 0.43031272284545874f;
    scalingDeCom[ 29 ] = 0.1650642834886438f;
    scalingDeCom[ 30 ] = 0.03490771432362905f;
    scalingDeCom[ 31 ] = 0.0031892209253436892f;

    buildOrthonormalSpace();
  }

}

package io.vacco.volach.wavelet.type;

public class VlDiscreteMayer extends VlWavelet {

  public VlDiscreteMayer( ) {
    this.transformWavelength = 2;
    this.motherWavelength = 62;
    this.scalingDeCom = new float[this.motherWavelength];

    scalingDeCom[ 0 ] = 0.0f;
    scalingDeCom[ 1 ] = -1.009999956941423e-12f;
    scalingDeCom[ 2 ] = 8.519459636796214e-09f;
    scalingDeCom[ 3 ] = -1.111944952595278e-08f;
    scalingDeCom[ 4 ] = -1.0798819539621958e-08f;
    scalingDeCom[ 5 ] = 6.066975741351135e-08f;
    scalingDeCom[ 6 ] = -1.0866516536735883e-07f;
    scalingDeCom[ 7 ] = 8.200680650386481e-08f;
    scalingDeCom[ 8 ] = 1.1783004497663934e-07f;
    scalingDeCom[ 9 ] = -5.506340565252278e-07f;
    scalingDeCom[ 10 ] = 1.1307947017916706e-06f;
    scalingDeCom[ 11 ] = -1.489549216497156e-06f;
    scalingDeCom[ 12 ] = 7.367572885903746e-07f;
    scalingDeCom[ 13 ] = 3.20544191334478e-06f;
    scalingDeCom[ 14 ] = -1.6312699734552807e-05f;
    scalingDeCom[ 15 ] = 6.554305930575149e-05f;
    scalingDeCom[ 16 ] = -0.0006011502343516092f;
    scalingDeCom[ 17 ] = -0.002704672124643725f;
    scalingDeCom[ 18 ] = 0.002202534100911002f;
    scalingDeCom[ 19 ] = 0.006045814097323304f;
    scalingDeCom[ 20 ] = -0.006387718318497156f;
    scalingDeCom[ 21 ] = -0.011061496392513451f;
    scalingDeCom[ 22 ] = 0.015270015130934803f;
    scalingDeCom[ 23 ] = 0.017423434103729693f;
    scalingDeCom[ 24 ] = -0.03213079399021176f;
    scalingDeCom[ 25 ] = -0.024348745906078023f;
    scalingDeCom[ 26 ] = 0.0637390243228016f;
    scalingDeCom[ 27 ] = 0.030655091960824263f;
    scalingDeCom[ 28 ] = -0.13284520043622938f;
    scalingDeCom[ 29 ] = -0.035087555656258346f;
    scalingDeCom[ 30 ] = 0.44459300275757724f;
    scalingDeCom[ 31 ] = 0.7445855923188063f;
    scalingDeCom[ 32 ] = 0.44459300275757724f;
    scalingDeCom[ 33 ] = -0.035087555656258346f;
    scalingDeCom[ 34 ] = -0.13284520043622938f;
    scalingDeCom[ 35 ] = 0.030655091960824263f;
    scalingDeCom[ 36 ] = 0.0637390243228016f;
    scalingDeCom[ 37 ] = -0.024348745906078023f;
    scalingDeCom[ 38 ] = -0.03213079399021176f;
    scalingDeCom[ 39 ] = 0.017423434103729693f;
    scalingDeCom[ 40 ] = 0.015270015130934803f;
    scalingDeCom[ 41 ] = -0.011061496392513451f;
    scalingDeCom[ 42 ] = -0.006387718318497156f;
    scalingDeCom[ 43 ] = 0.006045814097323304f;
    scalingDeCom[ 44 ] = 0.002202534100911002f;
    scalingDeCom[ 45 ] = -0.002704672124643725f;
    scalingDeCom[ 46 ] = -0.0006011502343516092f;
    scalingDeCom[ 47 ] = 6.554305930575149e-05f;
    scalingDeCom[ 48 ] = -1.6312699734552807e-05f;
    scalingDeCom[ 49 ] = 3.20544191334478e-06f;
    scalingDeCom[ 50 ] = 7.367572885903746e-07f;
    scalingDeCom[ 51 ] = -1.489549216497156e-06f;
    scalingDeCom[ 52 ] = 1.1307947017916706e-06f;
    scalingDeCom[ 53 ] = -5.506340565252278e-07f;
    scalingDeCom[ 54 ] = 1.1783004497663934e-07f;
    scalingDeCom[ 55 ] = 8.200680650386481e-08f;
    scalingDeCom[ 56 ] = -1.0866516536735883e-07f;
    scalingDeCom[ 57 ] = 6.066975741351135e-08f;
    scalingDeCom[ 58 ] = -1.0798819539621958e-08f;
    scalingDeCom[ 59 ] = -1.111944952595278e-08f;
    scalingDeCom[ 60 ] = 8.519459636796214e-09f;
    scalingDeCom[ 61 ] = -1.009999956941423e-12f;

    buildOrthonormalSpace();

  }
}

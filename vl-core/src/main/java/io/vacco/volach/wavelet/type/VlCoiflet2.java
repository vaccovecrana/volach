package io.vacco.volach.wavelet.type;

/**
 * Ingrid Daubechies' orthonormal Coiflet wavelet of 12 coefficients.
 */
public class VlCoiflet2 extends VlWavelet {

  public VlCoiflet2() {
    this.transformWavelength = 2;
    this.motherWavelength = 12;

    this.scalingDeCom = new float[motherWavelength];
    scalingDeCom[0] = -0.0007205494453645122f;
    scalingDeCom[1] = -0.0018232088707029932f;
    scalingDeCom[2] = 0.0056114348193944995f;
    scalingDeCom[3] = 0.023680171946334084f;
    scalingDeCom[4] = -0.0594344186464569f;
    scalingDeCom[5] = -0.0764885990783064f;
    scalingDeCom[6] = 0.41700518442169254f;
    scalingDeCom[7] = 0.8127236354455423f;
    scalingDeCom[8] = 0.3861100668211622f;
    scalingDeCom[9] = -0.06737255472196302f;
    scalingDeCom[10] = -0.04146493678175915f;
    scalingDeCom[11] = 0.016387336463522112f;

    buildOrthonormalSpace();
  }
}

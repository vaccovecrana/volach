package io.vacco.volach.schema;

import java.io.Serializable;

public class VlFftSample implements Serializable {

  private static final long serialVersionUID = VlConstants.schemaVersion;

  public float[] real;
  public float[] realQtr;
  public float[] imaginary;

  public long sampleOffset;
  public int sampleFftOffset;
  public int sampleHashCode;

  public float[] getComposite() {
    float[] composite = new float[real.length];
    for (int i = 0; i < real.length / 2; i++) {
      composite[i * 2] = real[i];
      composite[i * 2 + 1] = -imaginary[i];
    }
    return composite;
  }

  public VlFftSample withSampleOffset(long sampleOffset) {
    this.sampleOffset = sampleOffset;
    return this;
  }

  public VlFftSample withSampleFftOffset(int sampleFftOffset) {
    this.sampleFftOffset = sampleFftOffset;
    return this;
  }

  public static VlFftSample from(int signalHashCode, float[] real, float[] realQtr, float[] imaginary) {
    VlFftSample sample = new VlFftSample();

    sample.sampleHashCode = signalHashCode;
    sample.real = new float[real.length];
    sample.realQtr = new float[realQtr.length];
    sample.imaginary = new float[imaginary.length];

    System.arraycopy(real, 0, sample.real, 0, real.length);
    System.arraycopy(realQtr, 0, sample.realQtr, 0, realQtr.length);
    System.arraycopy(imaginary, 0, sample.imaginary, 0, imaginary.length);

    return sample;
  }

}
package io.vacco.volach.dsp;

public class FftSampleF {

  public float[] real;
  public float[] imaginary;
  public float[] composite;

  public static FftSampleF from(float[] real, float[] imaginary) {
    FftSampleF sample = new FftSampleF();

    sample.real = real;
    sample.imaginary = imaginary;
    sample.composite = new float[real.length];

    for (int i = 0; i < real.length / 2; i++) {
      sample.composite[i * 2] = real[i];
      sample.composite[i * 2 + 1] = -imaginary[i];
    }

    return sample;
  }
}

package io.vacco.volach.dsp;

public class VlFftSample {

  public float[] real;
  public float[] imaginary;
  public float[] composite;

  public static VlFftSample from(float[] real, float[] imaginary) {
    VlFftSample sample = new VlFftSample();

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

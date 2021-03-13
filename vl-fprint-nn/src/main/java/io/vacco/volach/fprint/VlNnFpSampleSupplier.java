package io.vacco.volach.fprint;

import io.vacco.jtinn.net.JtPredictionSample;
import io.vacco.jtinn.net.JtPredictionSampleSupplier;
import io.vacco.volach.util.VlArrays;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.dto.VlAnalysisChunk;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import java.util.Iterator;
import java.util.function.Supplier;

public class VlNnFpSampleSupplier implements JtPredictionSampleSupplier {

  private Iterator<VlAnalysisChunk> activeChunks;
  private final VlAnalysisParameters params;
  private final Supplier<Integer> trackIdSupplier;

  private JtPredictionSample[] samples;
  private final double[]
      buff0 = new double[32],
      buff1 = new double[32],
      buff2 = new double[64];

  private double[][] freqBuff;

  public VlNnFpSampleSupplier(Supplier<Integer> trackIdSupplier, VlAnalysisParameters params) {
    this.params = params;
    this.trackIdSupplier = trackIdSupplier;
  }

  public void setLabel(int trackId, VlAnalysisChunk chunk) {
    VlArrays.packPair(trackId, chunk.signal.offset, buff0, buff1, buff2);
  }

  public void setFeatures(VlAnalysisChunk chunk) {
    if (freqBuff == null) {
      freqBuff = new double[chunk.samples.length][chunk.samples[0].freqPower.capacity()];
    }
    for (int k = 0; k < chunk.samples.length; k++) {
      for (int j = 0; j < chunk.samples[k].freqPower.capacity(); j++) {
        freqBuff[k][j] = chunk.samples[k].freqPower.get(j);
      }
    }
  }

  private void checkNext() {
    try {
      if (!activeChunks.hasNext()) {
        this.activeChunks = VlWaveletPacketAnalysisExtractor.from(params).iterator();
      }
    } catch (Exception e) {
      this.activeChunks = VlWaveletPacketAnalysisExtractor.from(params).iterator();
    }
  }

  public int getInputSize() {
    checkNext();
    return activeChunks.next().samples[0].freqPower.capacity();
  }

  @Override public JtPredictionSample[] get() {
    checkNext();

    VlAnalysisChunk c = activeChunks.next();
    setFeatures(c);
    setLabel(trackIdSupplier.get(), c);

    if (samples == null) { samples = new JtPredictionSample[c.samples.length]; }
    for (int k = 0; k < samples.length; k++) {
      if (samples[k] == null) {
        samples[k] = new JtPredictionSample();
      }
      samples[k].features = freqBuff[k];
      samples[k].labels = buff2;
    }

    return samples;
  }
}

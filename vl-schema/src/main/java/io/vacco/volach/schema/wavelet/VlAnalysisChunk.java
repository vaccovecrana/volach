package io.vacco.volach.schema.wavelet;

import io.vacco.volach.schema.audio.VlSignalChunk;
import java.util.Objects;

public class VlAnalysisChunk {

  public final VlSignalChunk signal;
  public final VlAnalysisSample[] samples;

  public VlAnalysisChunk(VlSignalChunk signal, VlAnalysisSample[] samples) {
    this.signal = Objects.requireNonNull(signal);
    this.samples = Objects.requireNonNull(samples);
  }
}

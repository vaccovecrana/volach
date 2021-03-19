package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.dto.VlAnalysisSample;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class VlPeakAnalysisExtractor extends Spliterators.AbstractSpliterator<float[][]> {

  private final VlWaveletPacketAnalysisExtractor extractor;
  private final int freqBands;
  private float[][] freqBuffer;

  public VlPeakAnalysisExtractor(VlWaveletPacketAnalysisExtractor extractor, int freqBands) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = Objects.requireNonNull(extractor);
    this.freqBands = freqBands;
  }

  private void readToLimit(VlAnalysisSample[] in) {
    if (freqBuffer == null) {
      freqBuffer = new float[in.length][freqBands];
    }
    for (int k = 0; k < in.length; k++) {
      for (int j = 0; j < freqBands; j++) {
        freqBuffer[k][j] = Math.abs(in[k].freqPower.get(j));
      }
    }
  }

  @Override public boolean tryAdvance(Consumer<? super float[][]> action) {
    return extractor.tryAdvance(chunk -> {
      readToLimit(chunk.samples);
      action.accept(freqBuffer);
    });
  }

  public static Stream<float[][]> from(VlAnalysisParameters params, int freqBands) {
    return StreamSupport.stream(
        new VlPeakAnalysisExtractor(
            new VlWaveletPacketAnalysisExtractor(
                new VlSignalExtractor(params.src, params.analysisSampleSize, params.normalizeSamples),
                params.level, params.wavelet, params.order
            ), freqBands
        ), false
    );
  }

}

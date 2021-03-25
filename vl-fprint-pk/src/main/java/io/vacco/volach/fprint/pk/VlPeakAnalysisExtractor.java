package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.pk.dto.VlAnalysisRegion;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.dto.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class VlPeakAnalysisExtractor extends Spliterators.AbstractSpliterator<VlAnalysisRegion> {

  private final int freqBands;
  private float[][] freqBuffer;

  private final VlWaveletPacketAnalysisExtractor extractor;
  private final VlAnalysisRegion buffer = new VlAnalysisRegion();

  public VlPeakAnalysisExtractor(VlWaveletPacketAnalysisExtractor extractor, int freqBands) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = Objects.requireNonNull(extractor);
    this.freqBands = freqBands;
  }

  private void readToLimit(VlAnalysisSample[] in) {
    if (freqBuffer == null) {
      freqBuffer = new float[in.length][freqBands];
      buffer.spectrum = freqBuffer;
    }
    for (int k = 0; k < in.length; k++) {
      for (int j = 0; j < freqBands; j++) {
        freqBuffer[k][j] = Math.abs(in[k].freqPower.get(j));
      }
    }
    buffer.samples = in;
  }

  @Override public boolean tryAdvance(Consumer<? super VlAnalysisRegion> action) {
    return extractor.tryAdvance(chunk -> {
      readToLimit(chunk.samples);
      action.accept(buffer);
    });
  }

  public static Stream<VlAnalysisRegion> from(VlAnalysisParameters params, int freqBands) {
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

package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.pk.dto.VlAnalysisRegion;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.VlWpNode;
import io.vacco.volach.wavelet.dto.*;
import io.vacco.volach.wavelet.type.VlBattle23;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class VlPeakAnalysisExtractor extends Spliterators.AbstractSpliterator<VlAnalysisRegion> {

  public static final VlAnalysisParameters referenceParams = VlAnalysisParameters.from(
      null, 65535, 9, true,
      new VlBattle23(), VlWpNode.Order.Sequency
  );

  private final int freqBands;
  private float[][] freqBuffer;

  private final VlWaveletPacketAnalysisExtractor extractor;
  private final VlAnalysisRegion regionBuffer = new VlAnalysisRegion();

  public VlPeakAnalysisExtractor(VlWaveletPacketAnalysisExtractor extractor, int freqBands) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = Objects.requireNonNull(extractor);
    this.freqBands = freqBands;
  }

  @Override public boolean tryAdvance(Consumer<? super VlAnalysisRegion> action) {
    return extractor.tryAdvance(chunk -> {
      if (freqBuffer == null || chunk.samples.length != freqBuffer.length) {
        freqBuffer = new float[chunk.samples.length][freqBands];
        regionBuffer.spectrum = freqBuffer;
      }
      for (int k = 0; k < chunk.samples.length; k++) {
        for (int j = 0; j < freqBands; j++) {
          freqBuffer[k][j] = Math.abs(chunk.samples[k].freqPower.get(j));
        }
      }
      regionBuffer.chunk = chunk;
      action.accept(regionBuffer);
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

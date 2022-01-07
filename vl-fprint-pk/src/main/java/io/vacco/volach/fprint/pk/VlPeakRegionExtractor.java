package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.pk.dto.*;
import io.vacco.volach.wavelet.VlAudioIOParameters;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class VlPeakRegionExtractor extends Spliterators.AbstractSpliterator<VlAnalysisRegion> {

  private float[][] freqBuffer;

  private final VlAnalysisParameters analysisParams;
  private final VlWaveletPacketAnalysisExtractor extractor;
  private final VlAnalysisRegion regionBuffer = new VlAnalysisRegion();

  public VlPeakRegionExtractor(VlWaveletPacketAnalysisExtractor extractor, VlAnalysisParameters ap) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = Objects.requireNonNull(extractor);
    this.analysisParams = ap;
  }

  @Override public boolean tryAdvance(Consumer<? super VlAnalysisRegion> action) {
    int freqBands = analysisParams.frequencyCutoff;
    return extractor.tryAdvance(chunk -> {
      if (freqBuffer == null || chunk.samples.length != freqBuffer.length) {
        freqBuffer = new float[chunk.samples.length][freqBands];
        regionBuffer.spectrum = freqBuffer;
      }
      for (int k = 0; k < chunk.samples.length; k++) {
        for (int j = 0; j < freqBands; j++) {
          freqBuffer[k][j] = Math.abs(chunk.samples[k].freqPower[j]);
        }
      }
      regionBuffer.chunk = chunk;
      action.accept(regionBuffer);
    });
  }

  public static Stream<VlAnalysisRegion> from(VlAudioIOParameters iop, VlAnalysisParameters ap) {
    return StreamSupport.stream(
        new VlPeakRegionExtractor(
            new VlWaveletPacketAnalysisExtractor(
                new VlSignalExtractor(iop.src, iop.analysisSampleSize, iop.scaleToUnit),
                iop.level, iop.wavelet, iop.order
            ), ap
        ), false
    );
  }

}

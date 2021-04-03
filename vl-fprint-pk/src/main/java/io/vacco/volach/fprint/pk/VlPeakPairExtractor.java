package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.pk.dto.*;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.dto.VlAudioIOParameters;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class VlPeakPairExtractor extends Spliterators.AbstractSpliterator<List<VlPeakPair>> {

  private final List<VlPeakPair> pairs = new ArrayList<>();
  private final VlPeakAnchorExtractor extractor;
  private final double dMin, dMax;

  public VlPeakPairExtractor(VlPeakAnchorExtractor extractor, double dMin, double dMax) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = extractor;
    this.dMin = dMin;
    this.dMax = dMax;
  }

  @Override public boolean tryAdvance(Consumer<? super List<VlPeakPair>> action) {
    return extractor.tryAdvance(region -> {
      pairs.clear();
      for (int i = 0; i < region.anchorPoints.size(); i++) {
        VlAnchorPoint pI = region.anchorPoints.get(i);
        for (int j = 0; j < region.anchorPoints.size(); j++) {
          if (i != j) {
            VlAnchorPoint pJ = region.anchorPoints.get(j);
            if (pI.xA() < pJ.xA()) {
              double dist = pI.distanceTo(pJ);
              if (dMin < dist && dist < dMax) {
                pairs.add(VlPeakPair.from(pI, pJ));
              }
            }
          }
        }
      }
      action.accept(pairs);
    });
  }

  public static Stream<List<VlPeakPair>> from(VlAudioIOParameters p, VlAnalysisParameters ap) {
    return StreamSupport.stream(
        new VlPeakPairExtractor(
            new VlPeakAnchorExtractor(
                new VlPeakRegionExtractor(
                    new VlWaveletPacketAnalysisExtractor(
                        new VlSignalExtractor(
                            p.src, p.analysisSampleSize, p.scaleToUnit, p.normalizationOffset
                        ), p.level, p.wavelet, p.order
                    ), ap
                ), ap
            ), ap.peakDistanceMin, ap.peakDistanceMax
        ), false
    );
  }

}

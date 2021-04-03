package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.pk.dto.*;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.dto.VlAudioIOParameters;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import static io.vacco.volach.fprint.pk.VlFprintArrays.*;
import static io.vacco.volach.util.VlArrays.flatten;

public class VlPeakAnchorExtractor extends Spliterators.AbstractSpliterator<VlAnalysisRegion> {

  private final Map<Integer, Map<Float, VlAnchorPoint>> peakIdx = new HashMap<>();
  private final VlPeakRegionExtractor extractor;
  private final VlAnalysisParameters params;


  private final float[][] spectrumBuffer;
  private final double[] feature;

  public VlPeakAnchorExtractor(VlPeakRegionExtractor in, VlAnalysisParameters p) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = in;
    this.params = p;
    this.spectrumBuffer = new float[p.netRegionSize][p.netRegionSize];
    this.feature = new double[p.netRegionSize * p.netRegionSize];
  }

  @Override public boolean tryAdvance(Consumer<? super VlAnalysisRegion> action) {
    return extractor.tryAdvance(region -> {
      peakIdx.clear();

      int regionLength = region.chunk.samples.length;
      int regionOffset = region.chunk.samples[0].hilbertOffset;
      int hr = params.netRegionSizeHalf;

      for (int j = hr; j < regionLength - hr; j += hr) {
        for (int k = hr; k < region.spectrum[0].length - hr; k += params.netYSlide) {
          regionSquare(region.spectrum, j, k, spectrumBuffer);
          flatten(spectrumBuffer, feature);

          VlAnchorPoint lp = VlAnchorPoint.maxLocal(spectrumBuffer);
          lp.type = VlPeakType.fromRaw(params.network.estimate(feature));
          lp.xOff = regionOffset + j;
          lp.yOff = k;

          if (lp.type != null && lp.magnitude > params.peakMagnitudeThreshold) {
            VlAnchorPoint lp0 = peakIdx.computeIfAbsent(lp.xA(), j0 -> new HashMap<>()).get(lp.magnitude);
            if (lp0 == null || lp.yD() < lp0.yD() && lp.xD() < lp0.xD()) {
              peakIdx.get(lp.xA()).put(lp.magnitude, lp);
              lp.region = new float[spectrumBuffer.length][spectrumBuffer.length];
              lp.valid = true;
              copy(spectrumBuffer, lp.region);
            }
          }
        }
      }

      region.anchorPoints.clear();
      region.anchorPoints.addAll(
          peakIdx.values().stream()
              .flatMap(m -> m.values().stream())
              .sorted(Comparator.comparing(VlAnchorPoint::sortKey))
              .collect(Collectors.toList())
      );
      action.accept(region);
    });
  }

  public static Stream<VlAnalysisRegion> from(VlAudioIOParameters p, VlAnalysisParameters ap) {
    return StreamSupport.stream(
        new VlPeakAnchorExtractor(
            new VlPeakRegionExtractor(
                new VlWaveletPacketAnalysisExtractor(
                    new VlSignalExtractor(
                        p.src, p.analysisSampleSize, p.scaleToUnit, p.normalizationOffset
                    ), p.level, p.wavelet, p.order
                ), ap
            ), ap
        ), false);
  }

}

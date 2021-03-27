package io.vacco.volach.fprint.pk;

import io.vacco.jtinn.net.JtNetwork;
import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.fprint.pk.dto.*;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import static io.vacco.volach.fprint.pk.VlFprintArrays.*;
import static io.vacco.volach.fprint.pk.dto.VlAnalysisRegion.*;
import static io.vacco.volach.util.VlArrays.flatten;

public class VlPeakAnchorExtractor extends Spliterators.AbstractSpliterator<VlAnalysisRegion> {

  private final Map<Integer, Map<Float, VlAnchorPoint>> peakIdx = new HashMap<>();
  private final VlPeakAnalysisExtractor extractor;
  private final JtNetwork net;

  private final double peakMagnitudeThreshold;
  private final float[][] spectrumBuffer = new float[RegionSize][RegionSize];
  private final double[] feature = new double[RegionSize * RegionSize];

  public VlPeakAnchorExtractor(double peakMagnitudeThreshold, VlPeakAnalysisExtractor in, JtNetwork net) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.extractor = in;
    this.net = net;
    this.peakMagnitudeThreshold = peakMagnitudeThreshold;
  }

  private boolean inRangeX(int x) { return x >= XMin && x <= XMax; }

  @Override public boolean tryAdvance(Consumer<? super VlAnalysisRegion> action) {
    return extractor.tryAdvance(region -> {
      peakIdx.clear();

      int regionLength = region.chunk.samples.length;
      int regionOffset = region.chunk.samples[0].hilbertOffset;

      for (int j = HalfReg; j < regionLength - HalfReg; j += HalfReg) {
        for (int k = HalfReg; k < region.spectrum[0].length - HalfReg; k += YSlide) {
          regionSquare(region.spectrum, j, k, spectrumBuffer);
          flatten(spectrumBuffer, feature);

          VlAnchorPoint lp = VlAnchorPoint.maxLocal(spectrumBuffer);
          lp.type = VlPeakType.fromRaw(net.estimate(feature));
          lp.xOff = regionOffset + j;
          lp.yOff = k;

          if (lp.type != null && lp.magnitude > peakMagnitudeThreshold && inRangeX(lp.x)) {
            VlAnchorPoint lp0 = peakIdx.computeIfAbsent(j, j0 -> new HashMap<>()).get(lp.magnitude);
            if (lp0 == null || lp.yD() < lp0.yD()) {
              peakIdx.get(j).put(lp.magnitude, lp);
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

  public static Stream<VlAnalysisRegion> from(VlAnalysisParameters params, JtNetwork net,
                                              int freqBands, double peakMagnitudeThreshold) {
    return StreamSupport.stream(
        new VlPeakAnchorExtractor(peakMagnitudeThreshold,
            new VlPeakAnalysisExtractor(
                new VlWaveletPacketAnalysisExtractor(
                    new VlSignalExtractor(
                        params.src, params.analysisSampleSize, params.normalizeSamples
                    ), params.level, params.wavelet, params.order
                ), freqBands
            ), net
        ), false);
  }

}

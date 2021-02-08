package io.vacco.volach.fprint;

import io.vacco.oruzka.hash.OzXxHash;
import io.vacco.volach.wavelet.VlAnalysisSample;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class VlXXHashFingerPrintExtractor extends Spliterators.AbstractSpliterator<VlXXHashFingerPrint[]> {

  public static final byte version = 1;
  public static final long seed = 72398472934L;

  private final double peakCutoff;
  private final VlWaveletPacketAnalysisExtractor analysisExtractor;

  public VlXXHashFingerPrintExtractor(double peakCutOff, VlWaveletPacketAnalysisExtractor analysisExtractor) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.peakCutoff = peakCutOff;
    this.analysisExtractor = Objects.requireNonNull(analysisExtractor);
  }

  @Override
  public boolean tryAdvance(Consumer<? super VlXXHashFingerPrint[]> onFingerPrints) {
    Map<Float, Integer> freqBins = new TreeMap<>();
    return analysisExtractor.tryAdvance(chunk -> {
      VlXXHashFingerPrint[] fingerPrints = new VlXXHashFingerPrint[chunk.samples.length];
      for (int i = 0; i < chunk.samples.length; i++) {
        VlAnalysisSample sample = chunk.samples[i];

        freqBins.clear();
        for (int k = 0; k < sample.freqPower.capacity(); k++) {
          float val = sample.freqPower.get(k);
          if (Math.abs(val) > peakCutoff) {
            freqBins.put(sample.freqPower.get(k), k);
          }
        }

        VlXXHashFingerPrint fingerPrint = new VlXXHashFingerPrint();
        fingerPrint.freqBinsHash = hashOf(freqBins);
        fingerPrint.hilbertOffset = sample.hilbertOffset;
        fingerPrint.version = version;
        fingerPrints[i] = fingerPrint;
      }
      onFingerPrints.accept(fingerPrints);
    });
  }

  private long hashOf(Map<Float, Integer> freqs) {
    /*
    hash entry contents:
    [32 bits freq power, 32 bits freq bin] * # of detected frequencies.
     */
    int size = 8 * freqs.size();
    ByteBuffer hashData = ByteBuffer.allocate(size);
    freqs.forEach((val, bin) -> hashData.putFloat(val).putInt(bin));
    return OzXxHash.hash64(hashData.array(), 0, size, seed);
  }

  public static Stream<VlXXHashFingerPrint[]> from(double peakCutoff, VlWaveletPacketAnalysisExtractor analysisExtractor) {
    return StreamSupport.stream(new VlXXHashFingerPrintExtractor(peakCutoff, analysisExtractor), false);
  }
}

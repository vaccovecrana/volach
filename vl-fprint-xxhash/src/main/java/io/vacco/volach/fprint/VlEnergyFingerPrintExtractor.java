package io.vacco.volach.fprint;

import io.vacco.volach.wavelet.dto.VlAnalysisSample;
import io.vacco.volach.wavelet.VlWaveletPacketAnalysisExtractor;

import java.nio.FloatBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.vacco.volach.util.VlArrays.*;

public class VlEnergyFingerPrintExtractor extends Spliterators.AbstractSpliterator<VlEnergyFingerPrint[]> {

  public static final byte version = 1;
  private final VlWaveletPacketAnalysisExtractor analysisExtractor;

  public VlEnergyFingerPrintExtractor(VlWaveletPacketAnalysisExtractor analysisExtractor) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.analysisExtractor = Objects.requireNonNull(analysisExtractor);
  }

  private FloatBuffer frequencyEnergy(VlAnalysisSample sample) {
    FloatBuffer enb = floatBuffer(sample.freqPower.capacity());
    for (int k = 0; k < sample.freqPower.capacity(); k++) {
      float val = sample.freqPower.get(k);
      float enr = (float) Math.pow(Math.abs(val), 2);
      enb.put(k, enr);
    }
    return enb;
  }

  @Override
  public boolean tryAdvance(Consumer<? super VlEnergyFingerPrint[]> onFingerPrints) {
    return analysisExtractor.tryAdvance(chunk -> {
      VlEnergyFingerPrint[] fingerPrints = new VlEnergyFingerPrint[chunk.samples.length];
      // List<Float> freqs = new ArrayList<>();

      for (int j = 0; j < chunk.samples.length; j++) {
        VlAnalysisSample sample = chunk.samples[j];
        VlEnergyFingerPrint fp = new VlEnergyFingerPrint();
        FloatBuffer fEnr = frequencyEnergy(sample);
/*
        freqs.clear();
        for (int k = 0; k < fEnr.capacity(); k++) { freqs.add(fEnr.get(k)); }
        Collections.sort(freqs);
        Collections.reverse(freqs);
*/
        float enrAvg = 0;
        for (int i = 0; i < fEnr.capacity(); i++) {
          float enr = fEnr.get(i);
          enrAvg += enr;
        }
        enrAvg = enrAvg / fEnr.capacity();

        /*
        for (int i = 0; i < 32; i++) { // TODO where should I get this number from?
        }
         */

        fp.version = version;
        fp.hilbertOffset = sample.hilbertOffset;
        fp.energyAvg = enrAvg;
        fingerPrints[j] = fp;
      }

      onFingerPrints.accept(fingerPrints);
    });
  }

  public static Stream<VlEnergyFingerPrint[]> from(VlWaveletPacketAnalysisExtractor analysisExtractor) {
    return StreamSupport.stream(new VlEnergyFingerPrintExtractor(analysisExtractor), false);
  }
}

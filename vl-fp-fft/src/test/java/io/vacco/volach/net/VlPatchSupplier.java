package io.vacco.volach.net;

import io.vacco.jtinn.net.*;
import io.vacco.volach.schema.*;
import java.util.*;
import java.util.stream.Collectors;
import static io.vacco.volach.schema.VlArrays.*;

public class VlPatchSupplier implements JtPredictionSampleSupplier {

  private final List<VlTonePatch> patches;
  private final JtPredictionSample[] samples;

  public VlPatchSupplier(VlPatchGroup grp, VlStFtParams params) {
    this.patches = grp.sources.stream().flatMap(src -> src.patches.stream()).collect(Collectors.toList());
    this.samples = new JtPredictionSample[patches.size()];
    for (int i = 0; i < samples.length; i++) {
      samples[i] = JtPredictionSample.of(
          new double[params.regionWidth * params.regionHeight],
          new double[VlToneType.values().length]
      );
    }
  }

  @Override public JtPredictionSample[] get() {
    Collections.shuffle(patches);
    for (int i = 0; i < samples.length; i++) {
      JtPredictionSample smp = samples[i];
      VlTonePatch p = patches.get(i);
      flatten(p.spectrum, smp.features);
      smp.labels = p.toneType.flags;
    }
    return samples;
  }

}
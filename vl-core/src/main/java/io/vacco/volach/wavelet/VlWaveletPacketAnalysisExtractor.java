package io.vacco.volach.wavelet;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.schema.wavelet.VlAnalysisChunk;
import io.vacco.volach.schema.wavelet.VlAnalysisSample;
import io.vacco.volach.schema.wavelet.VlWpNode;
import io.vacco.volach.wavelet.type.VlWavelet;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class VlWaveletPacketAnalysisExtractor extends Spliterators.AbstractSpliterator<VlAnalysisChunk> {

  private final int level;
  private final VlWavelet wavelet;
  private final VlSignalExtractor extractor;
  private final VlWpNode.Order order;

  private int totalAnalysisSamples;

  public VlWaveletPacketAnalysisExtractor(VlSignalExtractor extractor, int level, VlWavelet wavelet, VlWpNode.Order order) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.level = level;
    this.order = order;
    this.wavelet = Objects.requireNonNull(wavelet);
    this.extractor = Objects.requireNonNull(extractor);
    VlWaveletTransform.validate(extractor.bufferSize, level, wavelet);
  }

  @Override
  public boolean tryAdvance(Consumer<? super VlAnalysisChunk> onAnalysisSamples) {
    return extractor.tryAdvance(chunk -> {
      VlWpNode root = VlWaveletPacketTransform.naturalTree(chunk.data, wavelet, level);
      if (order == VlWpNode.Order.Sequency) {
        root.asSequencyMutation();
      }

      VlWpNode[] nodes = VlWaveletPacketTransform.collect(root, level);
      VlAnalysisSample[] analysisSamples = VlAnalysisSample.from(totalAnalysisSamples, nodes);

      totalAnalysisSamples = totalAnalysisSamples + analysisSamples.length;
      onAnalysisSamples.accept(new VlAnalysisChunk(chunk, analysisSamples));
    });
  }

  public static Stream<VlAnalysisChunk> from(VlAudioIOParameters p) {
    return StreamSupport.stream(
        new VlWaveletPacketAnalysisExtractor(
            new VlSignalExtractor(p.src, p.analysisSampleSize, p.scaleToUnit),
            p.level, p.wavelet, p.order
        ), false
    );
  }

}

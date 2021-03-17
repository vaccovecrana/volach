package io.vacco.volach.wavelet;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.dto.VlAnalysisChunk;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.dto.VlAnalysisSample;
import io.vacco.volach.wavelet.type.VlWavelet;

import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

      if (extractor.eof) {
        long tn = extractor.totalSamples;
        int tnp = extractor.totalChunks * extractor.bufferSize;
        int wn = totalAnalysisSamples;
        int cutIdx = (int) ((tn * wn) / tnp);
        int diff = wn - cutIdx;
        VlAnalysisSample[] truncatedSamples = new VlAnalysisSample[analysisSamples.length - diff];
        System.arraycopy(analysisSamples, 0, truncatedSamples, 0, truncatedSamples.length);
        analysisSamples = truncatedSamples;
      }

      onAnalysisSamples.accept(new VlAnalysisChunk(chunk, analysisSamples));
    });
  }

  public static Stream<VlAnalysisChunk> from(VlAnalysisParameters params) {
    return StreamSupport.stream(
        new VlWaveletPacketAnalysisExtractor(
            new VlSignalExtractor(params.src, params.analysisSampleSize, params.normalizeSamples),
            params.level, params.wavelet, params.order
        ), false
    );
  }

}

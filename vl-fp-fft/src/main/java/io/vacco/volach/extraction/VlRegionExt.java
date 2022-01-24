package io.vacco.volach.extraction;

import io.vacco.jtinn.net.JtNetwork;
import io.vacco.jtinn.util.JtIo;
import io.vacco.volach.schema.*;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import static io.vacco.volach.schema.VlArrays.*;

public class VlRegionExt extends Spliterators.AbstractSpliterator<List<VlFftRegion>> {

  private final VlStFtParams stp;
  private final VlStFtExt stFt;
  private final VlFftSample[] fftBuff;

  private final Map<String, VlFftRegion> peakIdx = new TreeMap<>();

  private final JtNetwork net;
  private final double[] netBuffer;

  private VlFftSample cursor;
  private boolean eof = false;

  public VlRegionExt(URL audioSrc, VlStFtExt stFt, VlStFtParams stp) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    stFt.reset(new VlSampleExt(audioSrc, true));
    stFt.asStream().count();
    stFt.reset(new VlSampleExt(audioSrc, true));
    this.stFt = stFt;
    this.stp = stp;
    this.fftBuff = new VlFftSample[stp.regionWidth];
    this.net = JtIo.readNet(VlRegionExt.class.getResourceAsStream("/io/vacco/stft/net.ser"));
    this.netBuffer = new double[stp.regionWidth * stp.regionHeight];
  }

  private List<VlFftRegion> scan() {
    int freqBands = fftBuff[0].realQtr.length;
    if (freqBands % stp.regionHeight != 0) {
      String msg = String.format(
          "Invalid region height: [input: %d, height: %d]",
          fftBuff[0].realQtr.length, stp.regionHeight);
      throw new IllegalStateException(msg);
    }

    int k = 0;
    int freqIdx;
    List<VlFftRegion> rl = new ArrayList<>();
    do {
      freqIdx = k * stp.netFreqScanStepY;
      VlFftRegion r = VlFftRegion.from(freqIdx, stp.regionHeight, fftBuff);
      flatten(r.fftMtx, netBuffer);
      r.toneType = VlToneType.fromRaw(net.estimate(netBuffer), stp.netScanThreshold);
      if (r.toneType != null && r.peak.val > stp.netPowerThreshold) {
        rl.add(r);
      }
      k++;
    } while (freqIdx + stp.regionHeight < freqBands);

    return rl;
  }

  private VlFftSample nextFft() {
    eof = !stFt.tryAdvance(smp -> cursor = smp);
    if (eof) {
      return VlFftSample.from(-1,
          new float[cursor.real.length],
          new float[cursor.realQtr.length],
          new float[cursor.imaginary.length]
      );
    }
    return cursor;
  }

  private void fill() {
    int shiftDiff = stp.regionWidth - stp.netFreqScanStepX;
    System.arraycopy(fftBuff, stp.netFreqScanStepX, fftBuff, 0, shiftDiff);
    for (int i = 0; i < stp.netFreqScanStepX; i++) {
      fftBuff[i + shiftDiff] = null;
    }
    for (int k = 0; k < stp.regionWidth; k++) {
      if (fftBuff[k] == null) {
        fftBuff[k] = nextFft();
      }
    }
  }

  @Override public boolean tryAdvance(Consumer<? super List<VlFftRegion>> action) {
    if (eof) return false;
    fill();
    action.accept(scan());
    return !eof;
  }

  public Map<String, VlFftRegion> peakIndex() {
    if (peakIdx.isEmpty()) {
      Map<String, List<VlFftRegion>> idx = asStream()
          .flatMap(Collection::stream)
          .collect(Collectors.groupingBy(r -> r.peak.fftKey()));
      for (Map.Entry<String, List<VlFftRegion>> e : idx.entrySet()) {
        long straights = e.getValue().stream().filter(r -> r.toneType.equals(VlToneType.STRAIGHT)).count();
        long shifts = e.getValue().stream().filter(r -> r.toneType.equals(VlToneType.SHIFT)).count();
        e.getValue().stream()
            .filter(r -> straights > shifts ? r.toneType == VlToneType.STRAIGHT : r.toneType == VlToneType.SHIFT)
            .findFirst().ifPresent(r -> peakIdx.put(e.getKey(), r));
      }
    }
    return peakIdx;
  }

  public List<VlFftPeakPair> peakPairs(double minDistance, double maxDistance) {
    List<VlFftPeak> peaks = peakIndex().values().stream().map(r -> r.peak).collect(Collectors.toList());
    return peaks.stream().flatMap(pk0 ->
        peaks.stream()
            .filter(pk1 -> pk0.fftDistanceTo(pk1) > minDistance && pk0.fftDistanceTo(pk1) < maxDistance)
            .filter(pk1 -> pk0.fftOff < pk1.fftOff)
            .map(pk1 -> VlFftPeakPair.from(pk0, pk1))
    ).collect(Collectors.toList());
  }

  public Stream<List<VlFftRegion>> asStream() {
    return StreamSupport.stream(this, false);
  }

}

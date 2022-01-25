package io.vacco.volach.extraction;

import io.vacco.volach.impl.*;
import io.vacco.volach.schema.VlFftSample;
import java.io.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;
import static io.vacco.volach.schema.VlArrays.*;

public class VlStFtExt extends Spliterators.AbstractSpliterator<VlFftSample> {

  private final VlFft fft;
  private final VlHammingWindow window;
  private final Map<Integer, Serializable> fftCache;

  private Spliterator<Float> src;
  private final int hopDelta, hopSize;

  private boolean next, eof;
  public boolean ranged;

  private int  cursorSlice = 0;
  private long cursorSmp = 0;
  private long offsetSmp = 0;

  private float min = Float.MAX_VALUE, max = Float.MIN_VALUE;

  private final float[] buff;
  private final int[] i = new int[1];

  public VlStFtExt(VlFft fft, int hopSize, Map<Integer, Serializable> fftCache) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    if (hopSize < 1 || hopSize > fft.bufferSize) {
      throw new IllegalArgumentException(String.format("Invalid buffer/hop size: [%d, %d]", fft.bufferSize, hopSize));
    }
    this.fft = fft;
    this.window = new VlHammingWindow(fft.bufferSize);
    this.buff = new float[fft.bufferSize];
    this.hopSize = hopSize;
    this.hopDelta = buff.length - hopSize;
    this.fftCache = fftCache;
  }

  private void fill(int start, int n) {
    for (i[0] = 0; i[0] < n; i[0]++) {
      next = src.tryAdvance(v -> buff[start + i[0]] = v);
      cursorSmp = cursorSmp + 1;
      if (!next) {
        eof = true;
        buff[start + i[0]] = 0;
        ranged = true;
      }
    }
  }

  public VlStFtExt reset(Spliterator<Float> src) {
    this.src = (src);
    this.cursorSlice = 0;
    this.cursorSmp = 0;
    this.offsetSmp = 0;
    this.eof = false;
    return this;
  }

  @Override public boolean tryAdvance(Consumer<? super VlFftSample> action) {
    if (cursorSmp == 0) {
      fill(0, buff.length);
    } else {
      System.arraycopy(buff, hopSize, buff, 0, hopDelta);
      fill(hopDelta, hopSize);
    }

    offsetSmp = cursorSmp - buff.length;

    float[] slice = window.update(buff);
    Serializable sr = fftCache.computeIfAbsent(Arrays.hashCode(slice), sk -> {
      VlFftSample s0 = fft.apply(slice).withSampleOffset(offsetSmp).withSampleFftOffset(cursorSlice);
      for (int k = 0; k < s0.realQtr.length; k++) {
        s0.realQtr[k] = Math.abs(s0.realQtr[k]);
      }
      cursorSlice++;
      return s0;
    });
    VlFftSample s = (VlFftSample) sr;

    if (ranged) {
      normalize(s.realQtr, min, max);
    } else {
      min = min(s.realQtr, min);
      max = max(s.realQtr, max);
    }

    action.accept(s);
    return !eof;
  }

  public Stream<VlFftSample> asStream() {
    return StreamSupport.stream(this, false);
  }

}

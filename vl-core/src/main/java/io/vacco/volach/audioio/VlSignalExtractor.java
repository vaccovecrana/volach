package io.vacco.volach.audioio;

import io.vacco.volach.schema.audio.VlSignalChunk;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import static io.vacco.volach.audioio.VlMonoAudioInputStream.*;
import static io.vacco.volach.util.VlArrays.*;
import static io.vacco.volach.util.VlMath.*;

public class VlSignalExtractor extends Spliterators.AbstractSpliterator<VlSignalChunk> {

  private final byte[] sampleBuffer = new byte[2];

  private final float[] zeroBuffer;
  private final float[] signalBuffer;
  private final VlMonoAudioInputStream input;

  public boolean eof = false, scaleToUnit;
  public final int bufferSize;
  public long totalChunks;
  public long totalSamples;

  public VlSignalExtractor(URL src, int analysisSampleSize, boolean scaleToUnit) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.input = loadPcm16Le(src);
    this.bufferSize = nextPow2(analysisSampleSize);
    this.zeroBuffer = new float[bufferSize];
    this.signalBuffer = new float[bufferSize];
    this.scaleToUnit = scaleToUnit;
    Arrays.fill(zeroBuffer, 0);
  }

  @Override public boolean tryAdvance(Consumer<? super VlSignalChunk> onChunk) {
    try {
      int samplesRead;
      float sample;
      System.arraycopy(zeroBuffer, 0, signalBuffer, 0, signalBuffer.length);
      for (int k = 0; k < bufferSize; k++) {
        samplesRead = input.read(sampleBuffer, 0, sampleBuffer.length);
        eof = samplesRead == -1;
        if (eof) { break; }
        sample = (float) readSignedLe(sampleBuffer);
        sample = scaleToUnit ? (((sample * 2) + 1) / (float) 65535) : sample;
        signalBuffer[k] = sample;
        totalSamples++;
      }
      if (eof) { input.close(); }
      onChunk.accept(new VlSignalChunk(signalBuffer, totalChunks * bufferSize));
      totalChunks++;
      return !eof;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static Stream<VlSignalChunk> from(URL src, int analysisSampleSize, boolean scaleToUnit) {
    return StreamSupport.stream(new VlSignalExtractor(src, analysisSampleSize, scaleToUnit), false);
  }

}

package io.vacco.volach.audioio;

import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

import static io.vacco.volach.audioio.VlMonoAudioInputStream.*;
import static io.vacco.volach.util.VlArrays.*;
import static io.vacco.volach.util.VlMath.*;

public class VlSignalExtractor extends Spliterators.AbstractSpliterator<VlSignalChunk> {

  private final byte[] sampleBuffer = new byte[2];

  private final float normalizationOffset;
  private final float[] zeroBuffer;
  private final FloatBuffer signalBuffer;
  private final VlMonoAudioInputStream input;

  public boolean eof = false, scaleToUnit;
  public final int bufferSize;
  public int totalChunks;
  public long totalSamples;

  public VlSignalExtractor(URL src, int analysisSampleSize, boolean scaleToUnit, float normalizationOffset) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.input = loadPcm16Le(src);
    this.bufferSize = nextPow2(analysisSampleSize);
    this.zeroBuffer = new float[bufferSize];
    this.signalBuffer = floatBuffer(bufferSize);
    this.scaleToUnit = scaleToUnit;
    this.normalizationOffset = normalizationOffset;
  }

  @Override
  public boolean tryAdvance(Consumer<? super VlSignalChunk> onChunk) {
    try {
      int samplesRead;
      float sample;
      signalBuffer.rewind();
      signalBuffer.put(zeroBuffer);
      for (int k = 0; k < bufferSize; k++) {
        samplesRead = input.read(sampleBuffer, 0, sampleBuffer.length);
        if (eof = samplesRead == -1) { break; }
        sample = (float) readSignedLe(sampleBuffer);
        sample = scaleToUnit ? (((sample * 2) + 1) / (float) 65535) : sample;
        sample = sample > 0 ? sample + normalizationOffset : sample - normalizationOffset;
        signalBuffer.put(k, sample);
        totalSamples++;
      }
      if (eof) { input.close(); }
      totalChunks++;
      onChunk.accept(new VlSignalChunk(signalBuffer, totalChunks * bufferSize));
      return !eof;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public static FloatBuffer padPow2(FloatBuffer in) {
    FloatBuffer out = floatBuffer(nextPow2(in.capacity()));
    copy(in, 0, out, 0, in.capacity());
    return out;
  }

  public static Stream<VlSignalChunk> from(URL src, int analysisSampleSize, boolean scaleToUnit, float normalizationFactor) {
    return StreamSupport.stream(new VlSignalExtractor(src, analysisSampleSize, scaleToUnit, normalizationFactor), false);
  }

}

package io.vacco.volach.audioio;

import java.io.IOException;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static io.vacco.volach.audioio.VlMonoAudioInputStream.*;
import static io.vacco.volach.audioio.VlArrays.*;

public class VlSignalExtractor extends Spliterators.AbstractSpliterator<VlSignalChunk> {

  private final byte[] sampleBuffer = new byte[2];

  private final float[] zeroBuffer;
  private final FloatBuffer signalBuffer;
  private final VlMonoAudioInputStream input;

  private final int bufferSize;
  private int totalChunks;
  private long totalSamples = 0;
  private boolean eof = false;

  public VlSignalExtractor(URL src) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.input = loadPcm16Le(src);
    this.bufferSize = nextPow2((int) input.getFormat().getSampleRate());
    this.zeroBuffer = new float[bufferSize];
    this.signalBuffer = floatBuffer(bufferSize);
  }

  @Override
  public boolean tryAdvance(Consumer<? super VlSignalChunk> onChunk) {
    try {
      int samplesRead;
      signalBuffer.rewind();
      signalBuffer.put(zeroBuffer);
      for (int k = 0; k < bufferSize; k++) {
        samplesRead = input.read(sampleBuffer, 0, sampleBuffer.length);
        if (eof = samplesRead == -1) { break; }
        float sample = (float) readSignedLe(sampleBuffer);
        sample = (((sample * 2) + 1) / (float) 65535);
        signalBuffer.put(k, sample);
        totalSamples++;
      }
      onChunk.accept(new VlSignalChunk(signalBuffer, totalChunks, (long) totalChunks * bufferSize));
      totalChunks++;
      if (eof) {
        input.close();
      }
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

  private static int nextPow2(int x) {
    return x == 1 ? 1 : Integer.highestOneBit(x - 1) * 2;
  }

  private static int readSignedLe(byte[] in) {
    return (in[0] & 0xff) | (short) (in[1] << 8);
  }

  public static Stream<VlSignalChunk> from(URL src) {
    return StreamSupport.stream(new VlSignalExtractor(src), false);
  }
}

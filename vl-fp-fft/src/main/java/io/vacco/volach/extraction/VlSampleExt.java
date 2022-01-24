package io.vacco.volach.extraction;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.nio.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.*;

public class VlSampleExt extends Spliterators.AbstractSpliterator<Float> {

  private final byte[] sampleBuffer = new byte[2];
  private final VlDownMixIst input;

  private float sample;
  private int bytesRead;
  public boolean eof = false, scaleToUnit;

  public VlSampleExt(URL src, boolean scaleToUnit) {
    super(Long.MAX_VALUE, Spliterator.ORDERED | Spliterator.NONNULL);
    this.input = loadPcm16(src);
    this.scaleToUnit = scaleToUnit;
  }

  @Override public boolean tryAdvance(Consumer<? super Float> onChunk) {
    try {
      if (eof) return false;
      bytesRead = input.read(sampleBuffer, 0, sampleBuffer.length);
      eof = bytesRead == -1;
      if (eof) {
        input.close();
        return false;
      }
      sample = (float) readSigned(sampleBuffer, input.bigEndian);
      sample = scaleToUnit ? (sample / (float) 32767) : sample;
      onChunk.accept(sample);
      return !eof;
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  public VlDownMixIst loadPcm16(URL url) {
    try {
      AudioInputStream in = AudioSystem.getAudioInputStream(new BufferedInputStream(url.openStream()));
      AudioFormat in_format = in.getFormat();
      AudioFormat decoded_format = new AudioFormat(
          AudioFormat.Encoding.PCM_SIGNED, in_format.getSampleRate(), 16,
          in_format.getChannels(), in_format.getChannels() * 2,
          in_format.getFrameRate(), in_format.isBigEndian()
      );
      return new VlDownMixIst(AudioSystem.getAudioInputStream(decoded_format, in));
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  public int readSigned(byte[] in, boolean bigEndian) {
    return bigEndian
        ? ByteBuffer.wrap(in).order(ByteOrder.BIG_ENDIAN).getShort()
        : ByteBuffer.wrap(in).order(ByteOrder.LITTLE_ENDIAN).getShort();
  }

  public Stream<Float> asStream() {
    return StreamSupport.stream(this, false);
  }

}

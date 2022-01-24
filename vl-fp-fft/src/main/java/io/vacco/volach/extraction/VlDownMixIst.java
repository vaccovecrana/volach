package io.vacco.volach.extraction;

import javax.sound.sampled.*;
import java.io.*;

public class VlDownMixIst extends AudioInputStream {

  protected int inputChannels;
  protected AudioFormat newFormat;

  private final int outputFrameSize;
  public  final boolean bigEndian;

  private byte[] inputBytes;

  public VlDownMixIst(AudioInputStream input) {
    super(input, input.getFormat(), input.getFrameLength());
    this.inputChannels = input.getFormat().getChannels();
    if (inputChannels != 2) throw new IllegalArgumentException("expected exactly 2 input channels");
    this.newFormat =
        new AudioFormat(
            input.getFormat().getEncoding(),
            input.getFormat().getSampleRate(),
            input.getFormat().getSampleSizeInBits(),
            1,
            input.getFormat().getFrameSize() / input.getFormat().getChannels(),
            input.getFormat().getFrameRate(),
            input.getFormat().isBigEndian());
    this.outputFrameSize = frameSize / inputChannels;
    this.bigEndian = getFormat().isBigEndian();
  }

  public int read(byte[] b, int off, int len) throws IOException {
    int nFrames = len / outputFrameSize;

    if (inputBytes == null || inputBytes.length != nFrames * frameSize) {
      inputBytes = new byte[nFrames * frameSize];
    }

    int nInputBytes = super.read(inputBytes, 0, inputBytes.length);
    if (nInputBytes <= 0) return nInputBytes;

    for (int i = 0, j = off; i < nInputBytes; i += frameSize, j += outputFrameSize) {
      int sample = 0;
      for (int c = 0; c < inputChannels; c++) {
        byte lobyte;
        byte hibyte;
        if (!bigEndian) {
          lobyte = inputBytes[i];
          hibyte = inputBytes[i + 1];
        } else {
          lobyte = inputBytes[i + 1];
          hibyte = inputBytes[i];
        }
        sample += hibyte << 8 | lobyte & 0xFF;
      }
      sample /= inputChannels; // average the three samples
      byte lobyte = (byte) (sample & 0xFF);
      byte hibyte = (byte) (sample >> 8);
      if (!bigEndian) {
        b[j] = lobyte;
        b[j + 1] = hibyte;
      } else {
        b[j] = hibyte;
        b[j + 1] = lobyte;
      }
    }
    return nInputBytes / inputChannels;
  }

  public long skip(long n) throws IOException {
    return super.skip(n * inputChannels) / inputChannels;
  }

  public int available() throws IOException {
    int av = super.available();
    if (av <= 0) return av;
    return av / inputChannels;
  }

  public AudioFormat getFormat() {
    return newFormat;
  }

}

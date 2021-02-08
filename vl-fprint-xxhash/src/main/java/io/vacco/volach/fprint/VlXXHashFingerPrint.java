package io.vacco.volach.fprint;

public class VlXXHashFingerPrint {

  public byte version;
  public long freqBinsHash;
  public int hilbertOffset;

  @Override public String toString() {
    return String.format("fp[ver: %s, offset: %s, hash: %s]",
        version, hilbertOffset, Long.toHexString(freqBinsHash)
    );
  }
}

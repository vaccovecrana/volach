package io.vacco.volach.fprint;

public class VlEnergyFingerPrint {

  public byte version;
  public float energyAvg;
  public int hilbertOffset;

  @Override public String toString() {
    return String.format("fp[ver: %s, offset: %s, enrAvg: %s]",
        version, hilbertOffset, energyAvg
    );
  }
}

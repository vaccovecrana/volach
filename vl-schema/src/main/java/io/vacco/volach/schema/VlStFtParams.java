package io.vacco.volach.schema;

public class VlStFtParams {

  public int regionWidth, regionHeight;
  public int fftBufferSize, fftHopSize;
  public boolean fftDirect, audioScaleToUnit;

  public double netTrainError, netScanThreshold, netPowerThreshold;
  public int netFreqScanStepX, netFreqScanStepY;

  public int peakDistanceMin, peakDistanceMax;

  private VlStFtParams() {}

  public static VlStFtParams getDefault() {
    VlStFtParams p = new VlStFtParams();
    p.regionWidth = 32;
    p.regionHeight = 32;
    p.fftBufferSize = 2048;
    p.fftHopSize = 1024;
    p.fftDirect = true;
    p.audioScaleToUnit = true;
    p.netTrainError = 0.998;
    p.netScanThreshold = 0.96;
    p.netPowerThreshold = 0.12;
    p.netFreqScanStepX = p.regionWidth / 4;
    p.netFreqScanStepY = p.regionHeight / 8;
    p.peakDistanceMin = 20;
    p.peakDistanceMax = 80;
    return p;
  }

}

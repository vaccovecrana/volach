package io.vacco.volach.wavelet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VlDwPt {

  private static List<float[]> split(float[] in, int chunkSize) {
    List<float[]> out = new ArrayList<>();
    for (int i = 0; i < in.length; i += chunkSize) {
      float[] chunk = Arrays.copyOfRange(in, i, Math.min(in.length, i + chunkSize));
      out.add(chunk);
    }
    return out;
  }

  public static void extractTail(float[] signal, int level, int maxLevel, VlWavelet wavelet, VlWpNode... parents) {
    if (level == maxLevel) return;
    int lN = level + 1;
    int children = parents.length * 2;
    int sliceLength = signal.length / children;
    if (sliceLength < wavelet.motherWavelength) return;

    List<float[]> splits = split(VlWaveletTransform.forward(signal, lN, wavelet), sliceLength);
    VlWpNode[] wpc = new VlWpNode[splits.size()];

    for (int i = 0; i < splits.size(); i++) {
      boolean isDetail = i % 2 != 0;
      VlWpNode parent = parents[i / 2];
      wpc[i] = new VlWpNode(i, lN, isDetail, splits.get(i), parent);
      if (isDetail) {
        parent.detail = wpc[i];
      } else {
        parent.approx = wpc[i];
      }
    }
    extractTail(signal, lN, maxLevel, wavelet, wpc);
  }

  public static VlWpNode extract(float[] signal, VlWavelet wavelet, int level) {
    VlWpNode result = new VlWpNode(0, 0, false, signal, null);
    extractTail(signal, 0, level, wavelet, result);
    return result;
  }
}

package io.vacco.volach.wavelet;

import io.vacco.volach.schema.wavelet.VlWpNode;
import io.vacco.volach.wavelet.type.VlWavelet;
import java.util.Arrays;

import static java.util.Arrays.stream;

public class VlWaveletPacketTransform {

  public static float[][] split(float[] data, int chunkSize) {
    int length = data.length;
    float[][] dest = new float[(length + chunkSize - 1)/chunkSize][];
    int destIndex = 0;
    int stopIndex = 0;
    for (int startIndex = 0; startIndex + chunkSize <= length; startIndex += chunkSize) {
      stopIndex += chunkSize;
      dest[destIndex++] = Arrays.copyOfRange(data, startIndex, stopIndex);
    }
    if (stopIndex < length) {
      dest[destIndex] = Arrays.copyOfRange(data, stopIndex, length);
    }
    return dest;
  }

  public static void naturalTreeTail(float[] signal, int level, int maxLevel, VlWavelet wavelet, VlWpNode... parents) {
    if (level == maxLevel) return;
    int lN = level + 1;
    int children = parents.length * 2;
    int sliceLength = signal.length / children;
    if (sliceLength < wavelet.motherWavelength) return;

    float[] raw = VlWaveletTransform.forward(signal, lN, wavelet);
    float[][] splits = split(raw, sliceLength);
    VlWpNode[] wpc = new VlWpNode[splits.length];

    for (int i = 0; i < splits.length; i++) {
      VlWpNode.Type type = i % 2 == 0 ? VlWpNode.Type.Approximation : VlWpNode.Type.Detail;
      VlWpNode parent = parents[i / 2];
      wpc[i] = new VlWpNode(i, lN, type, splits[i], parent);
      if (type == VlWpNode.Type.Detail) {
        parent.right = wpc[i];
      } else {
        parent.left = wpc[i];
      }
    }
    naturalTreeTail(signal, lN, maxLevel, wavelet, wpc);
  }

  public static VlWpNode naturalTree(float[] signal, VlWavelet wavelet, int maxLevel) {
    VlWpNode result = new VlWpNode(0, 0, VlWpNode.Type.Approximation, signal, null);
    naturalTreeTail(signal, 0, maxLevel, wavelet, result);
    return result;
  }

  public static float[][] extractCoefficients(VlWpNode[] nodes) {
    return stream(nodes).map(n -> n.coefficients).toArray(float[][]::new);
  }

  public static VlWpNode[] collect(VlWpNode root, int level) {
    int[] counters = new int[2];
    root.forDepthFirst(node -> {
      if (node.level == level) {
        counters[0] = counters[0] + 1;
      }
    });
    VlWpNode[] nodes = new VlWpNode[counters[0]];
    root.forDepthFirst(node -> {
      if (node.level == level) {
        nodes[counters[1]] = node;
        counters[1] = counters[1] + 1;
      }
    });
    return nodes;
  }
}

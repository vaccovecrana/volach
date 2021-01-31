package io.vacco.volach.wavelet;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.stream;
import static io.vacco.volach.audioio.VlArrays.*;

public class VlWaveletPacketTransform {

  private static List<FloatBuffer> split(FloatBuffer in, int chunkSize) {
    List<FloatBuffer> out = new ArrayList<>();
    for (int i = 0; i < in.capacity(); i += chunkSize) {
      FloatBuffer chunk = copyOfRange(in, i, Math.min(in.capacity(), i + chunkSize));
      out.add(chunk);
    }
    return out;
  }

  public static void naturalTreeTail(FloatBuffer signal, int level, int maxLevel, VlWavelet wavelet, VlWpNode ... parents) {
    if (level == maxLevel) return;
    int lN = level + 1;
    int children = parents.length * 2;
    int sliceLength = signal.capacity() / children;
    if (sliceLength < wavelet.motherWavelength) return;

    List<FloatBuffer> splits = split(VlWaveletTransform.forward(signal, lN, wavelet), sliceLength);
    VlWpNode[] wpc = new VlWpNode[splits.size()];

    for (int i = 0; i < splits.size(); i++) {
      VlWpNode.Type type = i % 2 == 0 ? VlWpNode.Type.Approximation : VlWpNode.Type.Detail;
      VlWpNode parent = parents[i / 2];
      wpc[i] = new VlWpNode(i, lN, type, splits.get(i), parent);
      if (type == VlWpNode.Type.Detail) {
        parent.right = wpc[i];
      } else {
        parent.left = wpc[i];
      }
    }
    naturalTreeTail(signal, lN, maxLevel, wavelet, wpc);
  }

  public static VlWpNode naturalTree(FloatBuffer signal, VlWavelet wavelet, int maxLevel) {
    VlWpNode result = new VlWpNode(0, 0, VlWpNode.Type.Approximation, signal, null);
    naturalTreeTail(signal, 0, maxLevel, wavelet, result);
    return result;
  }

  public static FloatBuffer[] extractCoefficients(VlWpNode[] nodes) {
    return stream(nodes).map(n -> n.coefficients).toArray(FloatBuffer[]::new);
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

package io.vacco.volach.wavelet;

import java.util.stream.Stream;

import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.Objects.requireNonNull;

public class VlWpNode {

  public final float[] coefficients;
  public final int id, level;
  public final boolean isDetail;
  public VlWpNode parent, approx, detail;

  public VlWpNode(int id, int level, boolean isDetail, float[] coefficients, VlWpNode parent) {
    this.id = id;
    this.isDetail = isDetail;
    this.level = level;
    this.coefficients = requireNonNull(coefficients);
    this.parent = parent;
  }

  private static Stream<VlWpNode> collectTail(VlWpNode root) {
    return Stream.concat(Stream.of(root), root.approx == null ? Stream.empty() :
        Stream.concat(collectTail(root.approx), collectTail(root.detail))
    );
  }

  public static VlWpNode[] collect(VlWpNode root, int level) {
    return collectTail(root).filter(n -> n.level == level).toArray(VlWpNode[]::new);
  }

  public VlWpNode[] collectChildren() {
    VlWpNode[] out = collectTail(this).filter(n -> n.approx == null && n.detail == null).toArray(VlWpNode[]::new);
    return out;
  }

  public float[][] collectLeafSamples(int originalSignalLength) {
    float[][] raw = stream(collectChildren()).map(n -> n.coefficients).toArray(float[][]::new);
    int paddedLength = this.coefficients.length;
    int childrenCoefficientsLength = raw[0].length;
    int targetSampleLength = (originalSignalLength * childrenCoefficientsLength) / paddedLength;
    float[][] out = stream(raw).map(da -> copyOf(da, targetSampleLength)).toArray(float[][]::new);
    return out;
  }

  @Override
  public String toString() {
    return String.format("W%s,%s[%s%s%s, %s]", level, id,
        parent != null ? '^' : "", isDetail ? 'd' : 'a',
        level, coefficients.length);
  }
}

package io.vacco.volach.wavelet;

import java.nio.FloatBuffer;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

public class VlWpNode {

  public enum Type { Approximation, Detail }
  public enum Order { Natural, Sequency }

  public final FloatBuffer coefficients;
  public int level, levelId;
  public final Type type;
  public final String idLabel;

  public final VlWpNode parent;
  public VlWpNode left, right;

  public VlWpNode(int levelId, int level, Type type, FloatBuffer coefficients, VlWpNode parent) {
    this.levelId = levelId;
    this.type = type;
    this.level = level;
    this.coefficients = requireNonNull(coefficients);
    this.parent = parent;

    StringBuilder sb = new StringBuilder();
    VlWpNode n = this;
    while (n != null) {
      sb.append(n.level != 0 ? n.label() : "");
      n = n.parent;
    }

    this.idLabel = sb.length() == 0 ? "s" : sb.reverse().toString();
  }

  private char label() { return type == Type.Detail ? 'd' : 'a'; }

  private void levelUpdateTail(VlWpNode root) {
    if (root.left == null || root.right == null) { return; }
    root.left.levelId = 2 * root.levelId;
    root.right.levelId = (2 * root.levelId) + 1;
    levelUpdateTail(root.left);
    levelUpdateTail(root.right);
  }

  /**
   * - TODO this could be optimized to preserve a single set of coefficient values when creating a new analysis node.
   * - TODO implement method to reverse back into natural order mutation.
   */
  private void sequencyMutationTail(VlWpNode root) {
    if (root.left == null || root.right == null) { return; }
    if (root.levelId % 2 != 0) {
      VlWpNode vnTmp = root.left;
      root.left = root.right;
      root.right = vnTmp;
      levelUpdateTail(root);
    }
    sequencyMutationTail(root.left);
    sequencyMutationTail(root.right);
  }

  public void asSequencyMutation() {
    sequencyMutationTail(this);
  }

  private void forDepthFirstTail(VlWpNode root, Consumer<VlWpNode> onNode) {
    if (root.left != null) { forDepthFirstTail(root.left, onNode); }
    if (root.right != null) { forDepthFirstTail(root.right, onNode); }
    onNode.accept(root);
  }

  public void forDepthFirst(Consumer<VlWpNode> onNode) {
    forDepthFirstTail(this, onNode);
  }

  @Override
  public String toString() {
    return String.format(
        "W%s,%s[%s%s, %s]",
        level, levelId, parent != null ? '^' : "", label(), idLabel
    );
  }
}

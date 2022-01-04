package io.vacco.volach.fprint.pk.dto;

public class VlAnchorPoint {

  public int x, xh, y, yh;
  public int xOff, yOff, sampleOffset;

  public float magnitude;
  public float[][] region;

  public VlPeakType type;
  public boolean valid;

  public static VlAnchorPoint maxLocal(float[][] in, int sampleOffset) {
    VlAnchorPoint p = new VlAnchorPoint();
    p.x = 0;
    p.y = 0;
    p.xh = in.length / 2;
    p.yh = in[0].length / 2;
    p.sampleOffset = sampleOffset;

    for (int j = 0; j < in.length; j++) {
      for (int k = 0; k < in[0].length; k++) {
        if (in[j][k] > p.magnitude) {
          p.magnitude = in[j][k];
          p.x = j;
          p.y = k;
        }
      }
    }

    p.x = p.x - p.xh;
    p.y = p.y - p.yh;
    return p;
  }

  public int xD() { return x + xh; }
  public int yD() { return y + yh; }

  public int xA() { return xOff + x; }
  public int yA() { return yOff + y; }

  public double distanceTo(VlAnchorPoint p) {
    return Math.hypot(Math.abs(p.yA() - yA()), Math.abs(p.xA() - xA()));
  }

  @Override
  public String toString() {
    return String.format(
        "{v: %.9f, xh(%04d) + x(%04d) = %04d, yh(%04d) + y(%04d) = %04d, xa: %04d, ya: %04d, sk: %s}",
        magnitude, xh, x, xD(), yh, y, yD(), xA(), yA(), sortKey()
    );
  }

  public String sortKey() { return String.format("%06d%06d", xA(), yA()); }
}

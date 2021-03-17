package io.vacco.volach.fprint.pk;

public class VlKernel33 {

  public int kRows = 3, kCols = 3;
  public int kCenterX = 3 / 2, kCenterY = 3 / 2;

  public float[][] krn = new float[3][];

  public static VlKernel33 from(float nw, float n, float ne,
                                float w,  float c, float e,
                                float sw, float s, float se) {
    VlKernel33 k = new VlKernel33();
    k.krn[0] = new float[] {nw, n, ne};
    k.krn[1] = new float[] {w,  c, e};
    k.krn[2] = new float[] {sw, s, se};
    return k;
  }

  /** Data MUST be in row/column order */
  public void apply(float[][] in, float[][] out) {
    int i, j, ii, jj, n, m, mm, nn;
    int rows = in.length, cols = in[0].length;
    for (i = 0; i < rows; ++i) {
      for (j = 0; j < cols; ++j) {
        for (m = 0; m < kRows; ++m) {
          mm = kRows - 1 - m;
          for (n = 0; n < kCols; ++n) {
            nn = kCols - 1 - n;
            ii = i + (kCenterY - mm);
            jj = j + (kCenterX - nn);
            if (ii >= 0 && ii < rows && jj >= 0 && jj < cols) {
              out[i][j] += in[ii][jj] * krn[mm][nn];
            }
          }
        }
      }
    }
  }

}
package io.vacco.volach.schema;

import java.io.Serializable;
import java.util.Objects;

public class VlMatchPeak implements Serializable {

  private static final long serialVersionUID = VlConstants.schemaVersion;

  public VlFftPeak query, source;

  public static VlMatchPeak from(VlFftPeak query, VlFftPeak source) {
    VlMatchPeak p = new VlMatchPeak();
    p.query = Objects.requireNonNull(query);
    p.source = Objects.requireNonNull(source);
    return p;
  }

  @Override public String toString() {
    return String.format("%s, %s", query, source);
  }
}

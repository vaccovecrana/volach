package io.vacco.volach.extraction;

import io.vacco.filemap.IndexedFileMap;
import io.vacco.volach.schema.VlFftSample;
import java.io.*;

public class VlFftDiskMap extends IndexedFileMap<Integer, VlFftSample> {
  public VlFftDiskMap(File data) {
    super(data);
  }
}

package io.vacco.volach.extraction;

import io.vacco.lash.*;
import io.vacco.lash.serde.*;
import java.io.*;

public class VlFftDiskMap extends DiskMap<Integer, Serializable> {
  public VlFftDiskMap(File dir) {
    super(IntSerde.getInstance(), SerializingSerde.getInstance(), new BucketDiskMap(dir));
  }
}

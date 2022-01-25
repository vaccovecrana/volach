package io.vacco.filemap;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface FileMap<K, V> extends Map<K, V>, Closeable {

  File getFile();

  long diskSize() throws IOException;

}

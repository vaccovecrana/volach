package io.vacco.filemap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Map;

import static io.vacco.filemap.B64Io.*;

/**
 * This abstract class is a utility to easily manipulate lines in a random access file and converting
 * text lines into key/value format and vice-versa.
 *
 * @author dagnelies
 */
public abstract class AbstractFileMap<K, V> implements FileMap<K, V> {

  protected File file;
  protected BufferedRandomAccessFile fileio;

  private static final String MODE = "rw";
  private long entriesWritten;

  public AbstractFileMap(File file) {
    try {
      this.file = file;
      init();
      if (fileio != null)
        fileio.close();

      entriesWritten = 0;
      fileio = new BufferedRandomAccessFile(file, MODE);

      while (!fileio.isEOF()) {
        long offset = fileio.pos();
        String line = fileio.readLine();
        if (line == null || line.isEmpty() || line.startsWith("#"))
          continue;
        loadEntry(offset, line);
        entriesWritten++;
      }
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  protected long getEntriesWritten() {
    return entriesWritten;
  }

  abstract protected void init() throws IOException;

  protected abstract void loadEntry(long offset, String line) throws IOException;

  public File getFile() {
    return file;
  }

  public class LineEntry implements Entry<K, V> {

    String line;
    int tabPos;

    LineEntry(String line) throws IOException {
      this.line = line;
      this.tabPos = line.indexOf('\t');
      if (tabPos <= 0) {
        throw new IOException("Failed to parse line: " + line);
      }
    }

    public String getKeyB64() {
      return line.substring(0, tabPos);
    }

    @Override public K getKey() { return fromB64(getKeyB64()); }
    @Override public V getValue() { return fromB64(getValueB64()); }
    public String getValueB64() { return line.substring(tabPos + 1); }
    @Override public V setValue(V value) {
      throw new RuntimeException("This operation is not supported.");
    }
  }

  protected Entry<K, V> parseLine(String line) throws IOException {
    int i = line.indexOf('\t');
    if (i <= 0) {
      throw new IOException("Failed to parse line: " + line);
    }
    String k64 = line.substring(0, i);
    String v64 = line.substring(i + 1);
    K key = fromB64(k64);
    V value = fromB64(v64);
    return new AbstractMap.SimpleEntry<>(key, value);
  }

  protected K parseKey(String line) throws IOException {
    int i = line.indexOf('\t');
    if (i <= 0) {
      throw new IOException("Failed to parse line: " + line);
    }
    return fromB64(line.substring(0, i));
  }

  protected V parseValue(String line) throws IOException {
    int i = line.indexOf('\t');
    if (i <= 0) {
      throw new IOException("Failed to parse line: " + line);
    }
    return fromB64(line.substring(i + 1));
  }

  protected String readLine(long offset) throws IOException {
    fileio.seek(offset);
    return fileio.readLine();
  }

  protected long writeLine(K key, V value) {
    try {
      entriesWritten++;

      String k64 = toB64(key);
      String v64 = toB64(value);
      String line = k64 + "\t" + v64 + "\n";

      long offset = fileio.length();
      fileio.seek(offset);
      fileio.write(line.getBytes(StandardCharsets.UTF_8));
      return offset;
    } catch (IOException e) {
      throw new RuntimeException("Failed to save entry for " + key, e);
    }
  }

  @Override
  public synchronized void putAll(Map<? extends K, ? extends V> m) {
    for (Entry<? extends K, ? extends V> entry : m.entrySet())
      put(entry.getKey(), entry.getValue());
  }

  protected synchronized void clearLines() {
    try {
      fileio.seek(0);
      fileio.truncate(0);
      entriesWritten = 0;
    } catch (IOException e) {
      throw new RuntimeException("Failed to clear persistent map", e);
    }
  }

  /**
   * Returns an estimate of the file's content fragmentation. It is the ratio of obsolete data in the file.
   * When entries are frequently updated and removed, the old entries are still stored in the file.
   * For example, a fragmentation of 2/3 would mean that roughly 2/3 of the file is filled with obsolete content.
   */
  public double getFragmentation() {
    return 1.0 - ((double) this.size() / entriesWritten);
  }

  public long diskSize() {
    return fileio.length();
  }

  public void close() throws IOException {
    fileio.close();
  }

}

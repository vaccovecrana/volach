package io.vacco.lash;

import java.io.Closeable;
import java.util.Iterator;
import java.util.Map;

public interface IDiskMap extends Closeable {
  /**
   * Returns the value corresponding to the given key, or null if it is not
   * present.  Zero-width values (ie, a hash set) are supported.
   */
  byte[] get(byte[] k);

  /**
   * Inserts the given record in the map, and returns the previous value associated
   * with the given key, or null if there was none.
   */
  byte[] put(byte[] k, byte[] v);

  /**
   * Inserts the given record in the map, only if there was no previous value associated
   * with the key.  Returns null in the case of a successful insertion, or the value
   * previously (and currently) associated with the map.
   */
  byte[] putIfAbsent(byte[] k, byte[] v);

  /**
   * Remove the record associated with the given key, returning the previous value, or
   * null if there was none.
   */
  byte[] remove(byte[] k);

  /**
   * Remove the given key if it is currently mapped to the given value.
   * Returns true if successful.
   */
  boolean remove(byte[] k, byte[] v);

  /**
   * Replace the value associated with the given key with the given value, if
   * there was an existing value.
   * Returns the previously associated value, or null if there was none.
   */
  byte[] replace(byte[] k, byte[] v);

  /**
   * If the given k is currently associated with prevVal, replace it with
   * newVal.  Returns true if successful.
   */
  boolean replace(byte[] k, byte[] prevVal, byte[] newVal);

  /**
   * Returns true if the given key is mapped in the table.
   */
  boolean containsKey(byte[] k);

  /**
   * Number of inserted records.  O(1).
   */
  long size();

  void clear();

  /**
   * Returns an iterator over key-value pairs.  Neither the returned iterator
   * nor the Map.Entry values iterated over support mutation.
   */
  Iterator<Map.Entry<byte[], byte[]>> iterator();
}

package io.vacco.lash;

import java.io.File;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.vacco.lash.util.Hash;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class TestBucketDiskMap {
  @Test
  public void testConcurrentInserts() throws Exception {
    final File tmpDir = Files.createTempDirectory("testConcurrentInserts").toFile();
    final ADiskMap dmap = new BucketDiskMap(tmpDir);
    final int recsPerThread = 2000000;
    try {
      final Thread t1 = new Thread(() -> {
        for (long i = 0; i < recsPerThread; i++) {
          final byte[] k = InsertHelper.longToBytes(i);
          final byte[] v = InsertHelper.longToBytes(i + 1);
          dmap.put(k, v);
        }
      });
      final Thread t2 = new Thread(() -> {
        for (long i = recsPerThread; i < recsPerThread * 2; i++) {
          final byte[] k = InsertHelper.longToBytes(i);
          final byte[] v = InsertHelper.longToBytes(i + 1);
          dmap.put(k, v);
        }
      });
      final Thread t3 = new Thread(() -> {
        for (long i = recsPerThread * 2; i < recsPerThread * 3; i++) {
          final byte[] k = InsertHelper.longToBytes(i);
          final byte[] v = InsertHelper.longToBytes(i + 1);
          dmap.put(k, v);
        }
      });
      final Thread t4 = new Thread(() -> {
        for (long i = recsPerThread; i < recsPerThread * 4; i++) {
          final byte[] k = InsertHelper.longToBytes(i);
          final byte[] v = InsertHelper.longToBytes(i + 1);
          dmap.put(k, v);
        }
      });

      t1.start();
      t2.start();
      t3.start();
      t4.start();
      t1.join();
      t2.join();
      t3.join();
      t4.join();
      for (long i = 0; i < recsPerThread * 4; i++) {
        final byte[] k = InsertHelper.longToBytes(i);
        final byte[] v = dmap.get(k);
        assertEquals(i + 1, InsertHelper.bytesToLong(v));
      }
    } finally {
      dmap.close();
      dmap.delete();
    }
  }

  @Test
  public void testInserts() throws Exception {
    final File tmpDir = Files.createTempDirectory("testInserts").toFile();
    final ADiskMap dmap = new BucketDiskMap(tmpDir);
    final int recs = 8000000;
    try {
      for (long i = 0; i < recs; i++) {
        final byte[] k = InsertHelper.longToBytes(i);
        final byte[] v = InsertHelper.longToBytes(i + 1);
        dmap.put(k, v);
        final byte[] vOut = dmap.get(k);
        assertEquals("Insert & retrieval mismatch: " + i
            , i + 1
            , InsertHelper.bytesToLong(vOut));
      }
      for (long i = 0; i < recs; i++) {
        final byte[] k = InsertHelper.longToBytes(i);
        final byte[] v = dmap.get(k);
        assertEquals("Insert overwritten: " + i
            , i + 1
            , InsertHelper.bytesToLong(v));
      }
    } finally {
      dmap.close();
      dmap.delete();
    }
  }

  @Test
  public void testHashCollisions() throws Exception {
    final File tmpDir = Files.createTempDirectory("testHashCollisions").toFile();
    final ADiskMap dmap = new BucketDiskMap(tmpDir);
    try {
      final long[] collisions = new long[500];
      collisions[0] = 1;
      for (int idx = 1; idx < collisions.length; idx++) {
        collisions[idx] = Hash.findCollision(collisions[idx - 1], 16);
      }

      for (int i = 0; i < collisions.length; i++) {
        final long v = collisions[i] + 1;
        final byte[] kBytes = InsertHelper.longToBytes(collisions[i]);
        final byte[] vBytes = InsertHelper.longToBytes(v);
        dmap.put(kBytes, vBytes);
        final byte[] vOutBytes = dmap.get(kBytes);
        final long vOut = InsertHelper.bytesToLong(vOutBytes);

        assertEquals("Insert/retrieval failure on index " + i, v, vOut);
      }
      for (int idx = 0; idx < collisions.length; idx++) {
        final long i = collisions[idx];
        final byte[] k = InsertHelper.longToBytes(i);
        final byte[] v = dmap.get(k);
        assertEquals("Unexpected inequality on index " + idx + "."
            , i + 1
            , InsertHelper.bytesToLong(v));
      }
    } finally {
      dmap.close();
      dmap.delete();
    }
  }

  @Test
  public void testIteration() throws Exception {
    final File tmpDir = Files.createTempDirectory("testIteration").toFile();
    final ADiskMap dmap = new BucketDiskMap(tmpDir);
    final ConcurrentHashMap<Long, Long> m = new ConcurrentHashMap<>(1000);
    final Random rng = new Random();
    final long nInserts = 1000;
    try {
      for (long k = 0; k < nInserts; k++) {
        final byte[] kBytes = InsertHelper.longToBytes(k);
        final long v = rng.nextLong();
        final byte[] vBytes = InsertHelper.longToBytes(v);
        m.put(k, v);
        dmap.put(kBytes, vBytes);
      }
      final Iterator<Map.Entry<byte[], byte[]>> it = dmap.iterator();
      for (long i = 0; i < nInserts; i++) {
        final Map.Entry<byte[], byte[]> e = it.next();
        final long k = InsertHelper.bytesToLong(e.getKey());
        final long v = InsertHelper.bytesToLong(e.getValue());
        //Does iterator value match what was inserted?
        assertEquals(m.get(k).longValue(), v);
        m.remove(k);
        it.remove();
      }
      //Does iteration hit all elements, and no more?
      assertFalse(it.hasNext());
      assertTrue(m.isEmpty());
      assertEquals(0, dmap.size());
      for (long i = 0; i < nInserts; i++) {
        final byte[] kBytes = InsertHelper.longToBytes(i);
        assertFalse(dmap.containsKey(kBytes));
      }
    } finally {
      dmap.close();
      dmap.delete();
    }
  }
}

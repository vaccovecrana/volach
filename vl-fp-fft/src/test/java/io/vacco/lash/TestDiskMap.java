package io.vacco.lash;

import java.io.File;
import java.nio.file.Files;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import io.vacco.lash.ADiskMap;
import io.vacco.lash.BucketDiskMap;
import io.vacco.lash.DiskMap;
import io.vacco.lash.serde.IntSerde;

@RunWith(JUnit4.class)
public class TestDiskMap {
  @Test
  public void testSerdes() throws Exception {
    final File tmpDir = Files.createTempDirectory("testSerdes").toFile();
    final ADiskMap backing = new BucketDiskMap(tmpDir);
    final DiskMap<Integer, Integer> dmap = new DiskMap<>(IntSerde.getInstance(), IntSerde.getInstance(), backing);

    final ConcurrentHashMap<Integer, Integer> hmap = new ConcurrentHashMap<>();
    final Random rng = new Random();

    //10M tries, 1M keys
    for (int i = 0; i < 10000000; i++) {
      final int k = rng.nextInt(1000000);
      final int v = rng.nextInt(1000000);
      switch (rng.nextInt(4)) {
        case 0:
          dmap.put(k, v);
          hmap.put(k, v);
          break;
        case 1:
          dmap.remove(k);
          hmap.remove(k);
          break;
        case 2:
          dmap.replace(k, v);
          hmap.replace(k, v);
          break;
        case 3:
          dmap.putIfAbsent(k, v);
          hmap.putIfAbsent(k, v);
          break;
      }
    }

    assertEquals(dmap, hmap);
    backing.delete();
  }
}

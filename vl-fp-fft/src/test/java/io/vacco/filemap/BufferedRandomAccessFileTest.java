package io.vacco.filemap;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BufferedRandomAccessFileTest {

  @Test public void test() throws IOException {
    BufferedRandomAccessFile bf = new BufferedRandomAccessFile(new File("./build//this-is-a-test.txt"), "rw");

    // clear the file
    bf.truncate(0);

    int N = 1000 * 100;
    long t = System.currentTimeMillis();
    int count;
    long pos_1234 = 0;
    // write N lines
    for (int i = 0; i < N; i++) {
      String uuid = UUID.randomUUID().toString();
      if (i == 1234) {
        pos_1234 = bf.pos();
        System.out.println("Writing line 1234 at pos " + pos_1234 + ": " + uuid);
      }
      bf.write(uuid + "\n");
    }

    System.out.println("Inserted " + N + " lines in " + (System.currentTimeMillis() - t) + " ms");
    t = System.currentTimeMillis();

    // skip N lines
    bf.seek(0);
    count = 0;
    while (!bf.isEOF()) {
      bf.skipUntil((byte) '\n');
      count++;
    }
    assert N == count;
    System.out.println("Skip " + count + " lines in " + (System.currentTimeMillis() - t) + " ms");

    // read N lines
    bf.seek(0);
    count = 0;
    while (!bf.isEOF()) {
      bf.readLine();
      count++;
    }
    assert N == count;
    System.out.println("Read " + count + " lines in " + (System.currentTimeMillis() - t) + " ms");

    bf.seek(pos_1234);
    System.out.println("Reading line 1234 at pos " + pos_1234 + ": " + bf.readLine());
    bf.close();
  }

}

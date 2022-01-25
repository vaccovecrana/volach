package io.vacco.filemap;

import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.io.File;
import java.io.Serializable;
import java.util.UUID;

import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class IndexedFileMapTest {

  public static class MyGuid implements Serializable {
    private static final long serialVersionUID = 1;
    public String guid;
    MyGuid() {
      this.guid = UUID.randomUUID().toString();
    }
  }

  static {
    it("Uses an indexed file map", () -> {
      // create blank DB, even if it already exists
      File file = new File("./build/indexed-file-map.jkv");
      if (file.exists()) {
        file.delete();
      }

      AbstractFileMap<String, MyGuid> map = new IndexedFileMap<>(file);

      // insert and get single element
      MyGuid myGuid = new MyGuid();
      System.out.println(myGuid.guid);
      map.put("test", new MyGuid());
      myGuid = map.get("test");
      System.out.println(myGuid.guid);

      // reload it
      map.close();
      map = new IndexedFileMap<>(file);
      myGuid = map.get("test");
      System.out.println(myGuid.guid);

      System.out.println("Created");
      System.out.println("Size: " + map.size());
      System.out.println("lucky number: " + map.get("lucky number"));

      long next = System.currentTimeMillis() + 1000;
      int size = map.size();
      while (size < 1000 * 1000) {
        if (System.currentTimeMillis() > next) {
          next += 1000;
          int sizeNow = map.size();
          System.out.println("Entries: " + sizeNow / 1000 + "k");
          System.out.println("TPS: " + (sizeNow - size) / 1000 + "k/s");
          size = sizeNow;
          System.out.println("Free memory (mb): " + (Runtime.getRuntime().freeMemory() / 1000 / 1000));
        }

        map.put(UUID.randomUUID().toString(), new MyGuid());
      }
    });
  }
}

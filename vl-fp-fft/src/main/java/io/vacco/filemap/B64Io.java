package io.vacco.filemap;

import java.io.*;
import java.util.Base64;

public class B64Io {

  public static <T> String toB64(T t) {
    try {
      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(t);
      oos.close();
      return Base64.getEncoder().encodeToString(bos.toByteArray());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T fromB64(String t) {
    try {
      ByteArrayInputStream ais = new ByteArrayInputStream(Base64.getDecoder().decode(t));
      ObjectInputStream ois = new ObjectInputStream(ais);
      T ot = (T) ois.readObject();
      ois.close();
      return ot;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

}

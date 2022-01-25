package io.vacco.lash.serde;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class StringSerde implements Serde<String> {
  private StringSerde() {
  }

  private static final StringSerde inst = new StringSerde();

  public static StringSerde getInstance() {
    return inst;
  }

  private static final Charset utf8 = StandardCharsets.UTF_8;

  @Override
  public String fromBytes(byte[] d) {
    if (d == null) return null;
    return new String(d, utf8);
  }

  @Override
  public byte[] toBytes(String t) {
    if (t == null) return new byte[]{};
    return t.getBytes(utf8);
  }
}

package io.vacco.volach;

import java.io.File;

public class VlTestVals {

  public static final String sourceAudio = "/audio/title03-track02.ogg";

  public static final String[] dbSources = {
      "/audio/title00-track00.ogg",
      "/audio/title00-track01.ogg",
      "/audio/title01-track00.ogg",
      "/audio/title02-track00.ogg",
      "/audio/title02-track01.ogg",
      "/audio/title03-track00.ogg",
      "/audio/title03-track01.ogg",
      "/audio/title03-track02.ogg",
      "/audio/title04-track00.ogg",
      "/audio/title04-track01.ogg"
  };

  public static final long[] trackIds = {0, 1, 100, 200, 201, 300, 301, 302, 400, 401};

  public static final String queryAudio = "/audio/title03-track02-query00.ogg";

  public static final File fftCacheDir = new File("./build/fft-cache");

}

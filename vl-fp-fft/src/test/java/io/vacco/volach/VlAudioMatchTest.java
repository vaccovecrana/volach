package io.vacco.volach;

import com.google.gson.Gson;
import io.vacco.volach.extraction.VlFftDiskMap;
import io.vacco.volach.schema.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.io.*;
import java.net.URL;
import java.util.*;

import static j8spec.J8Spec.*;
import static io.vacco.volach.VlTestVals.*;
import static io.vacco.volach.impl.VlAudioMatch.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlAudioMatchTest {

  private static final Gson g = new Gson();

  static {
    it("Builds a fingerprint database",  () -> {
      VlStFtParams rp = VlStFtParams.getDefault();
      File fpDb = new File("./build/fp-db.json");
      VlFingerPrintIndex db = new VlFingerPrintIndex();
      VlFftDiskMap fm = new VlFftDiskMap(fftCacheFile);

      if (!fpDb.exists()) {
        for (int i = 0; i < dbSources.length; i++) {
          String audio = dbSources[i];
          long trackId = trackIds[i];

          System.out.printf("Fingerprinting [%s, %d]%n", audio, trackId);
          List<VlFftPeakPair> pairs = fingerPrint(VlAudioMatchTest.class.getResource(audio), rp, fm);
          indexTrack(trackId, pairs, db);
        }
        FileWriter fw = new FileWriter(fpDb);
        g.toJson(db, fw);
        fw.close();
      } else {
        db = g.fromJson(new FileReader(fpDb), VlFingerPrintIndex.class);
      }

      URL audioSrc = VlAudioMatchTest.class.getResource(queryAudio);
      List<VlFftPeakPair> queryPairs = fingerPrint(audioSrc, rp, fm);
      Map<Long, Map<String, VlMatchPeak>> fpMatches = align(queryPairs, db, 3);
      List<VlMatchRange> ranges = rangesOf(fpMatches, 302, sampleLengthOf(audioSrc));

      ranges.forEach(System.out::println);
      fm.close();
    });
  }
}

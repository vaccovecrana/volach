package io.vacco.volach.fprint.pk;

import io.vacco.volach.fprint.pk.dto.*;
import io.vacco.volach.schema.fprint.VlPeakPair;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.volach.VlAnalysisUtil.*;
import static io.vacco.volach.fprint.pk.VlFprintSpecUtil.*;
import static io.vacco.volach.fprint.pk.dto.VlAnalysisParameters.*;
import static j8spec.J8Spec.it;

@RunWith(J8SpecRunner.class)
public class VlPairExtractionSpec {

  public static class VlDatabase extends TreeMap<String, List<VlPeakPair>> implements Serializable {
    public static final long serialVersionUID = 0;
  }

  static {
    it("It extracts anchors from sample dataset", () -> {
      File dbJson = new File("./build/query-db.json");
      File dbBin = new File("./build/query-db.ser");
      VlDatabase database = dbJson.exists() ? json.fromJson(VlDatabase.class, dbJson) : new VlDatabase();

      withAudio(querySources[10][1], audio -> {
        VlAnalysisParameters analysisParams = forReference();
        analysisParams.network = VlPeakPairExtractor.loadDefaultNet();

        if (database.isEmpty()) {
          for (int i = 0; i < querySources.length; i++) {
            String[] src = querySources[i];
            System.out.printf("Fingerprinting [%s] - ", src[0]);
            audioIoParams.src = VlPairExtractionSpec.class.getResource(src[0]);
            List<VlPeakPair> pairs = VlPeakPairExtractor.fromFlat(audioIoParams, analysisParams).collect(Collectors.toList());
            for (VlPeakPair p : pairs) { p.trackId = i; }
            database.putAll(pairs.stream().collect(Collectors.groupingBy(VlPeakPair::freqId)));
            System.out.printf("index contains [%03d] reference hashes%n", database.size());
            json.toJson(database, dbJson);
          }
          new ObjectOutputStream(new FileOutputStream(dbBin)).writeObject(database);
          System.out.printf("Fingerprint JSON index saved at [%s]%n", dbJson.getAbsolutePath());
          System.out.printf("Fingerprint Binary index saved at [%s]%n", dbBin.getAbsolutePath());
        } else {
          System.out.printf("Fingerprint index found at [%s]%n", dbJson.getAbsolutePath());
        }

        System.out.println("\nFingerprinting recorded sample...");
        audioIoParams.src = audio;
        analysisParams = forQuery();

        List<VlPeakPair> smpPairs = VlPeakPairExtractor.fromFlat(audioIoParams, analysisParams).collect(Collectors.toList());
        System.out.printf("Extracted [%03d] recording hashes%n", smpPairs.size());

        Map<Integer, Map<String, VlPeakPair>> diffIdx = VlPeakPairExtractor.align(smpPairs, database);
        Map<Integer, Integer> matchIdx = VlPeakPairExtractor.countMatches(diffIdx);

        System.out.println(matchIdx);
      });
    });
  }

}

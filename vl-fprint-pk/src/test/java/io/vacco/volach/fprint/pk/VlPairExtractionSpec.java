package io.vacco.volach.fprint.pk;

import io.vacco.volach.audioio.VlSampleNormalizer;
import io.vacco.volach.fprint.pk.dto.VlAnalysisParameters;
import io.vacco.volach.fprint.pk.dto.VlPeakPair;
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

  /*
   * TODO when fingerprinting reference data, set a high peak threshold (noise resistance).
   * When querying, set a low peak threshold (higher chance for surviving peak pairs).
   */
  static {
    it("It extracts anchors from sample dataset", () -> {
      File dbJson = new File("./build/query-db.json");
      File dbBin = new File("./build/query-db.ser");
      VlDatabase database = dbJson.exists() ? mapper.readValue(dbJson.toURI().toURL(), VlDatabase.class) : new VlDatabase();

      withAudio(querySources[10][1], audio -> {

        VlAnalysisParameters analysisParams = forReference();
        analysisParams.network = loadNet();

        if (database.isEmpty()) {
          for (int i = 0; i < querySources.length; i++) {
            String[] src = querySources[i];
            System.out.printf("Fingerprinting [%s] - ", src[0]);
            audioIoParams.src = VlPairExtractionSpec.class.getResource(src[0]);
            List<VlPeakPair> pairs = VlPeakPairExtractor.from(audioIoParams, analysisParams)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(p -> p.hilbertDelta))
                .collect(Collectors.toList());
            for (VlPeakPair p : pairs) { p.trackId = i; }
            pairs.stream().collect(Collectors.groupingBy(VlPeakPair::id)).forEach(database::put);
            System.out.printf("Fingerprint index contains [%03d] reference hashes%n", database.size());
          }
          mapper.writerWithDefaultPrettyPrinter().writeValue(dbJson, database);
          new ObjectOutputStream(new FileOutputStream(dbBin)).writeObject(database);
          System.out.printf("Fingerprint JSON index saved at [%s]%n", dbJson.getAbsolutePath());
          System.out.printf("Fingerprint Binary index saved at [%s]%n", dbBin.getAbsolutePath());
        } else {
          System.out.printf("Fingerprint index found at [%s]%n", dbJson.getAbsolutePath());
        }

        System.out.println("\nFingerprinting recorded sample...");

        audioIoParams.src = audio;
        audioIoParams.normalizationOffset = new VlSampleNormalizer(audioIoParams, analysisParams.sampleNormalizationLimit).getOffset();
        analysisParams = forQuery();

        Map<String, List<VlPeakPair>> liveIdx = new TreeMap<>(
            VlPeakPairExtractor.from(audioIoParams, analysisParams)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(p -> p.hilbertDelta))
                .collect(Collectors.groupingBy(VlPeakPair::id))
        );
        System.out.printf("Extracted [%03d] recording hashes%n", liveIdx.size());

        Map<Integer, Integer> matchIdx = new TreeMap<>();
        for (Map.Entry<String, List<VlPeakPair>> e : liveIdx.entrySet()) {
          System.out.printf("\nRecorded pairs @ [%s]%n", e.getKey());
          e.getValue().forEach(System.out::println);
          List<VlPeakPair> refPairs = database.get(e.getKey());
          if (refPairs != null) {
            System.out.println("Reference pair matches");
            refPairs.forEach(System.out::println);
            refPairs.forEach(p -> {
              int matches = matchIdx.getOrDefault(p.trackId, 0);
              matchIdx.put(p.trackId, matches + 1);
            });
          } else {
            System.out.println("No reference matches...");
          }
        }

        System.out.println("Possible track matches:");
        matchIdx.forEach((k, v) -> System.out.printf("[track: %03d, matches: %03d]%n", k, v));
      });
    });
  }

}

package io.vacco.volach.fprint.pk;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.audioio.VlSampleNormalizer;
import io.vacco.volach.fprint.pk.dto.*;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.volach.VlAnalysisUtil.*;
import static io.vacco.volach.fprint.pk.dto.VlAnalysisParameters.*;
import static io.vacco.volach.fprint.pk.VlFprintSpecUtil.*;
import static j8spec.J8Spec.it;

@RunWith(J8SpecRunner.class)
public class VlAnchorExtractionSpec {
  static {

    int queryIdx = 0, queryType = 2;

    it("It extracts analysis regions from audio content", () -> withAudio(querySources[queryIdx][queryType], audio -> {
      VlTrainingDataSet outDs = new VlTrainingDataSet();
      VlTrainingDataSet.VlTrainingSource outSrc = new VlTrainingDataSet.VlTrainingSource();
      VlAnalysisParameters analysisParams = forReference();

      outSrc.file = audio.toString();
      outSrc.anchors = new ArrayList<>();
      outDs.sources = new ArrayList<>();
      outDs.sources.add(outSrc);

      analysisParams.network = loadNet();
      audioIoParams.src = audio.toURI().toURL();
      audioIoParams.normalizationOffset = queryType > 0 ? // normalize non-reference audio files
          new VlSampleNormalizer(audioIoParams, analysisParams.sampleNormalizationLimit).getOffset() : 0;

      VlUpdateListener listener = new VlUpdateListener();
      File detected = new File("./peak-training/peaks-detected.json");

      System.out.println("Writing spectrum data...");
      withWriter(outSpectrum, out -> VlPeakAnchorExtractor.from(audioIoParams, analysisParams).forEach(region -> {
        outSrc.anchors.addAll(region.anchorPoints);
        for (float[] frq : region.spectrum) {
          listener.onData(frq, out, true);
        }
      }));

      System.out.println("Writing peak detection JSON dataset...");
      mapper.writerWithDefaultPrettyPrinter().writeValue(detected, outDs);

      System.out.printf("Found [%d] anchors%n", outSrc.anchors.size());
      Map<Integer, List<VlAnchorPoint>> pointMap = outSrc.anchors.stream().collect(Collectors.groupingBy(VlAnchorPoint::xA));

      System.out.println("Writing anchor CSV data...");
      withWriter(outAnchors, out -> {
        for (int i = 0; i < listener.total; i++) {
          List<VlAnchorPoint> points = pointMap.get(i);
          for (int j = 0; j < analysisParams.frequencyCutoff; j++) {
            if (points == null) {
              out.printf("%.9f%s", 0.0, j == analysisParams.frequencyCutoff - 1 ? "" : ", ");
            } else {
              final int xi = i, yi = j;
              Optional<VlAnchorPoint> olp = points.stream().filter(p -> p.xA() == xi && p.yA() == yi).findFirst();
              if (olp.isPresent()) {
                out.printf("%.9f%s", olp.get().magnitude, j == analysisParams.frequencyCutoff - 1 ? "" : ", ");
              } else {
                out.printf("%.9f%s", 0.0, j == analysisParams.frequencyCutoff - 1 ? "" : ", ");
              }
            }
          }
          out.println();
        }
      });
    }));
  }

}

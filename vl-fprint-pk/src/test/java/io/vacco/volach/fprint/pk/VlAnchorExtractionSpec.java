package io.vacco.volach.fprint.pk;

import com.esotericsoftware.yamlbeans.YamlReader;
import io.vacco.jtinn.net.JtNetwork;
import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.fprint.pk.dto.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static io.vacco.volach.VlAnalysisUtil.*;
import static io.vacco.volach.fprint.pk.VlPeakAnalysisExtractor.referenceParams;
import static j8spec.J8Spec.it;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlAnchorExtractionSpec {

  static {
    it("It extracts anchors from sample dataset", () -> {
      File audio = new File("/Users/jjzazuet/Desktop/test-00-reference-recording.mp3");
      VlTrainingDataSet outDs = new VlTrainingDataSet();
      VlTrainingDataSet.VlTrainingSource outSrc = new VlTrainingDataSet.VlTrainingSource();
      YamlReader r = new YamlReader(new InputStreamReader(
          VlMatrixSpec.class.getResource("/io/vacco/volach/fprint/pk/net.yml").openStream()
      ));
      JtNetwork net = r.read(JtNetwork.class);

      outSrc.file = audio.getAbsolutePath();
      outSrc.anchors = new ArrayList<>();
      outDs.sources = new ArrayList<>();
      outDs.sources.add(outSrc);
      referenceParams.src = audio.toURI().toURL();

      VlUpdateListener listener = new VlUpdateListener();
      File outSpectrum = new File("./build/test-spec.csv");

      withWriter(outSpectrum, out -> VlPeakAnchorExtractor.from(
          referenceParams, net, VlAnalysisRegion.CutoffFreqBands, 0.025
      ).forEach(region -> {
        outSrc.anchors.addAll(region.anchorPoints);
        region.anchorPoints.forEach(System.out::println);
        for (float[] floats : region.spectrum) {
          listener.onData(floats, out, true);
        }
      }));

      File detected = new File("./peak-training/peaks-detected.json");
      mapper.writerWithDefaultPrettyPrinter().writeValue(detected, outDs);

      Map<Integer, List<VlAnchorPoint>> pointMap = outSrc.anchors.stream().collect(Collectors.groupingBy(VlAnchorPoint::xA));
      File outAnchors = new File("./build/test-anc.csv");
      int coff = VlAnalysisRegion.CutoffFreqBands;

      withWriter(outAnchors, out -> {
        for (int i = 0; i < listener.total; i++) {
          List<VlAnchorPoint> points = pointMap.get(i);
          for (int j = 0; j < coff; j++) {
            if (points == null) {
              out.printf("%.9f%s", 0.0, j == coff - 1 ? "" : ", ");
            } else {
              final int xi = i, yi = j;
              Optional<VlAnchorPoint> olp = points.stream().filter(p -> p.xA() == xi && p.yA() == yi).findFirst();
              if (olp.isPresent()) {
                out.printf("%.9f%s", olp.get().magnitude, j == coff - 1 ? "" : ", ");
              } else {
                out.printf("%.9f%s", 0.0, j == coff - 1 ? "" : ", ");
              }
            }
          }
          out.println();
        }
      });
    });
  }

}

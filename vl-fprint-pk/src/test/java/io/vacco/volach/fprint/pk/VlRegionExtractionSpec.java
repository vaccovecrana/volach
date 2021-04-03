package io.vacco.volach.fprint.pk;

import io.vacco.volach.VlUpdateListener;
import io.vacco.volach.audioio.VlSampleNormalizer;
import io.vacco.volach.fprint.pk.dto.VlAnalysisParameters;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.net.URL;

import static io.vacco.volach.VlAnalysisUtil.withWriter;
import static io.vacco.volach.fprint.pk.VlFprintSpecUtil.*;
import static io.vacco.volach.fprint.pk.dto.VlAnalysisParameters.*;
import static j8spec.J8Spec.it;

@RunWith(J8SpecRunner.class)
public class VlRegionExtractionSpec {

  static {
    it("It extracts anchor regions from audio content", () -> {
      VlAnalysisParameters analysisParams = forReference();
      withAudio(querySources[0][2], audio -> {
        audioIoParams.src = audio.toURI().toURL();
        audioIoParams.normalizationOffset = new VlSampleNormalizer(audioIoParams, analysisParams.sampleNormalizationLimit).getOffset();

        VlUpdateListener listener = new VlUpdateListener();
        System.out.println("Writing spectrum data...");

        withWriter(outSpectrum, out -> VlPeakRegionExtractor.from(audioIoParams, analysisParams).forEach(region -> {
          System.out.printf("Region [%s]%n", region.chunk.signal.offset);
          for (float[] frq : region.spectrum) {
            listener.onData(frq, out, true);
          }
        }));
        listener.done();
      });
    });
  }

}

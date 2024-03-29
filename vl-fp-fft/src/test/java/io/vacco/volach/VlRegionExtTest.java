package io.vacco.volach;

import io.vacco.volach.extraction.*;
import io.vacco.volach.impl.VlFft;
import io.vacco.volach.schema.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;
import java.util.*;

import static j8spec.J8Spec.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlRegionExtTest {

  static {
    it("Extracts STFT regions from audio content",  () -> {
      String audio = VlTestVals.sourceAudio;
      VlStFtParams rp = VlStFtParams.getDefault();
      VlStFtExt ext = new VlStFtExt(new VlFft(rp.fftBufferSize, rp.fftDirect), rp.fftHopSize);
      VlRegionExt rxt = new VlRegionExt(VlRegionExtTest.class.getResource(audio), ext, rp);

      Map<String, VlFftRegion> regs = rxt.peakIndex();
      System.out.printf("Got [%d] peak anchor regions.%n", regs.size());
      regs.entrySet().forEach(e -> System.out.println(e.toString()));

      List<VlFftPeakPair> peakPairs = rxt.peakPairs(rp.peakDistanceMin, rp.peakDistanceMax);
      for (VlFftPeakPair pkp : peakPairs) {
        System.out.println(pkp);
      }
    });
  }

}

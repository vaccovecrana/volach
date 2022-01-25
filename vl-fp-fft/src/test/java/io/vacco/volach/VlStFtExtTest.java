package io.vacco.volach;

import com.google.gson.Gson;
import io.vacco.volach.extraction.*;
import io.vacco.volach.impl.*;
import io.vacco.volach.schema.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import java.io.*;
import java.util.*;
import org.junit.runner.RunWith;

import static io.vacco.volach.VlTestVals.*;
import static j8spec.J8Spec.it;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlStFtExtTest {

  private static final Gson g = new Gson();

  private static void printChunked(int chunkSize, float[] a) {
    for (int i = 0; i < a.length; i += chunkSize) {
      float[] slice = Arrays.copyOfRange(a, i, Math.min(a.length, i + chunkSize));
      for (float v : slice) { System.out.print((v < 0 ? "" : " ") + String.format("%3.6f", v) + ", " ); }
      System.out.println();
    }
    System.out.println();
  }

  static {
    it("Can perform forward FFT on fixed input values", () -> {
      VlUpdateListener l = new VlUpdateListener();

      float[] inputF = g.fromJson(l.loadRes("/samples-fft.json"), float[].class);
      float[] fftOutputF = g.fromJson(l.loadRes("/samples-fft-output-composite.json"), float[].class);

      VlFft fft = new VlFft(512, true);
      VlHammingWindow w = new VlHammingWindow(512);

      inputF = w.update(inputF);

      VlFftSample fftSmp = fft.apply(inputF);

      printChunked(8, fftOutputF);
      System.out.println("================================");
      printChunked(8, fftSmp.getComposite());
      System.out.println("================================");
    });

    it("Can perform STFT on pre-computed samples", () -> {
      VlUpdateListener l = new VlUpdateListener();
      double[] input = g.fromJson(l.loadRes("/samples-eas.json"), double[].class);
      VlFftDiskMap fm = new VlFftDiskMap(fftCacheDir);
      VlStFtExt s = new VlStFtExt(new VlFft(256, true), 128, fm)
          .reset(Arrays.stream(input).mapToObj(v -> (float) v).spliterator());
      l.withWriter(new File("./build/stft-samples.csv"), p -> {
        s.asStream().forEach(smp -> {
          System.out.println(smp.sampleOffset);
          l.onData(smp.realQtr, p, true);
        });
        l.done();
      });
      fm.close();
    });

    it("Can perform STFT on an audio signal", () -> {
      VlUpdateListener l = new VlUpdateListener();
      VlStFtParams rp = VlStFtParams.getDefault();
      VlFftDiskMap fm = new VlFftDiskMap(fftCacheDir);

      l.withWriter(new File("./build/stft-audio.csv"), p -> {
        VlStFtExt s = new VlStFtExt(new VlFft(rp.fftBufferSize, rp.fftDirect), rp.fftHopSize, fm)
            .reset(new VlSampleExt(VlStFtExtTest.class.getResource(sourceAudio), rp.audioScaleToUnit));

        System.out.printf("Extracted [%d] FFT samples%n", s.asStream().count());

        s = s.reset(new VlSampleExt(VlStFtExtTest.class.getResource(sourceAudio), rp.audioScaleToUnit));
        s.asStream().forEach(smp -> l.onData(smp.realQtr, p, false));
        System.out.println("2nd pass done");
        l.done();
      });
      fm.close();
    });
  }
}

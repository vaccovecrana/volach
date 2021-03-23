package io.vacco.volach.fprint.pk;

import io.vacco.jtinn.activation.JtActivationFn;
import io.vacco.jtinn.activation.JtSigmoid;
import io.vacco.jtinn.error.JtErrorFn;
import io.vacco.jtinn.error.JtMeanSquaredError;
import io.vacco.jtinn.net.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

import static io.vacco.volach.VlSpecUtil.*;
import static io.vacco.volach.util.VlArrays.*;
import static j8spec.J8Spec.*;
import static org.junit.Assert.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlPeakTrainingSpec {

  public static int[] indexAt(float[][] region, int x0, int y0, float[][] global) {
    return new int[2];
  }

  static {
    float[][] example = {
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 7, 1, 1, 0, 0, 0},
        {0, 0, 1, 8, 1, 0, 0, 0},
        {0, 0, 1, 1, 9, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0},
    };

    float[][] expected = {
        {7, 1, 1},
        {1, 8, 1},
        {1, 1, 9}
    };

    double[] expectedFlat = {7, 1, 1, 1, 8, 1, 1, 1, 9};

    it("Extracts a sub-region of a matrix", () -> {
      float[][] out = new float[3][3];
      regionSquare(example, 4, 3, out);
      assertEquals(out[0][0], expected[0][0], 0.0);
      assertEquals(out[2][2], expected[2][2], 0.0);
    });

    it("Can flatten a matrix array", () -> {
      double[] flat = new double[9];
      flatten(expected, flat);
      for (int i = 0; i < flat.length; i++) {
        assertEquals(expectedFlat[i], flat[i], 0);
      }
      System.out.println(Arrays.toString(flat));
    });

    // TODO fix these paths!!!
    it("Performs region traversal across a sample source's spectrum data", () -> {

    });

    it("It performs peak training from sample dataset", () -> {
      JtActivationFn actFn = new JtSigmoid();
      JtErrorFn errFn = new JtMeanSquaredError();
      JtNetwork net = new JtNetwork().init(
          trainingRegionSize * trainingRegionSize, new JtRandomInitializer().init(154866485),
          new JtSgdUpdater().init(1.0, 1.0),
          new JtLayer().init(64, actFn),
          new JtOutputLayer().init(VlPeakType.values()[0].flags.length, actFn, errFn)
      );
      JtTrainer trainer = new JtTrainer(
          net, (n, epoch, error) -> {
            System.out.printf("Epoch: [%s], error: [%.6f]%n", epoch, error);
            return error != -1 && error < 0.0002;
          },
          new VlAnchorTrainingSupplier(mapper)
      );
      trainer.start();

      String file = "./build/sample-007.mp3-spectrum.json";
      VlTrainingDataSet.VlSampleSource src = mapper.readValue(
          new File(file), VlTrainingDataSet.VlSampleSource.class
      );

      VlTrainingDataSet outDs = new VlTrainingDataSet();
      VlTrainingDataSet.VlTrainingSource outSrc = new VlTrainingDataSet.VlTrainingSource();

      outSrc.file = file;
      outSrc.anchors = new ArrayList<>();
      outDs.sources = new ArrayList<>();
      outDs.sources.add(outSrc);

      float[][] buffer = new float[trainingRegionSize][trainingRegionSize];
      double[] feature = new double[trainingRegionSize * trainingRegionSize];

      Function<Integer, Boolean> inRange = (i -> i >= 8 && i <= 24); // TODO implement peak region merging: Map<Integer, Map<Double, VlAnchorPoint>>

      for (int j = 16; j < src.spectrum.length - 16; j += 16) {
        for (int k = 16; k < src.spectrum[0].length - 16; k += 4) {
          regionSquare(src.spectrum, j, k, buffer);
          flatten(buffer, feature);
          double[] out = net.estimate(feature);
          VlPeakType pt = VlPeakType.fromRaw(out);
          VlLocalPeak lp = VlLocalPeak.maxLocal(buffer);

          if (pt != null && lp.value > 0.09 && inRange.apply(lp.x) && inRange.apply(lp.y)) {
            System.out.printf("%s/%s - [%s, %s/%s], %s%n", lp, 127 - lp.y, j, k, 127 - k, pt);
            VlTrainingDataSet.VlAnchorPoint ap = new VlTrainingDataSet.VlAnchorPoint();
            float[][] region = new float[buffer.length][buffer.length];
            copy(buffer, region);
            ap.valid = true;
            ap.region = region;
            ap.type = pt;
            ap.x = j;
            ap.y = k;
            outSrc.anchors.add(ap);
          }
        }
      }

      File detected = new File("./peak-training/peaks-detected.json");
      mapper.writerWithDefaultPrettyPrinter().writeValue(detected, outDs);
    });

  }
}

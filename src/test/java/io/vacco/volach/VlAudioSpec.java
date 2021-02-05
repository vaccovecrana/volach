package io.vacco.volach;

import io.vacco.volach.audioio.VlSignalExtractor;
import io.vacco.volach.wavelet.*;
import j8spec.annotation.DefinedOrder;
import j8spec.junit.J8SpecRunner;
import org.junit.runner.RunWith;

import java.io.*;
import java.net.URL;
import java.nio.FloatBuffer;
import java.util.Arrays;

import static j8spec.J8Spec.it;
import static io.vacco.volach.VlSpecUtil.*;

@DefinedOrder
@RunWith(J8SpecRunner.class)
public class VlAudioSpec {

  static {
    it(
        "Can extract frequency data from audio content",
        () -> {
          float[] range = new float[2];
          float[] buffer = new float[4096];
          int level = 4;

          VlWavelet wavelet = new VlHaar1();
          URL url = VlAudioSpec.class.getResource("/eas.mp3");
          File values = new File("/home/jjzazuet/Desktop/values.txt");

          withWriter(values, out -> VlSignalExtractor.from(url).forEach(chunk -> {
            VlWpNode root = VlWaveletPacketTransform.naturalTree(chunk.data, wavelet, level);
            // root.asSequencyMutation();
            VlWpNode[] nodes = VlWaveletPacketTransform.collect(root, level);
            System.out.println(chunk);
            FloatBuffer[] coeffFreq = VlWaveletPacketTransform.extractCoefficients(nodes);
            for (FloatBuffer floatBuffer : coeffFreq) {
              floatBuffer.get(buffer);
              out.println(Arrays.toString(buffer).replace("[", "").replace("]", ""));
              for (float v : buffer) {
                if (v < range[0]) range[0] = v;
                if (v > range[1]) range[1] = v;
              }
            }
          }));

          System.out.printf("vmin=%s, vmax=%s", range[0] / 8, range[1] / 8);
        }
    );
  }

}

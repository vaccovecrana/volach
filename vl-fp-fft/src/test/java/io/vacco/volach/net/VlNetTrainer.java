package io.vacco.volach.net;

import com.google.gson.Gson;
import io.vacco.jtinn.activation.*;
import io.vacco.jtinn.error.*;
import io.vacco.jtinn.net.*;
import io.vacco.jtinn.util.JtIo;
import io.vacco.volach.extraction.*;
import io.vacco.volach.impl.VlFft;
import io.vacco.volach.schema.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static io.vacco.volach.schema.VlArrays.*;

public class VlNetTrainer {

  public static final Gson g = new Gson();
  public static final VlStFtParams rp = VlStFtParams.getDefault();

  public static void dumpSpectrum(URL audioSrc, File spectrumOut) throws IOException {
    File fftCache = new File("./build/net-trainer-fft-cache");
    VlFftDiskMap fm = new VlFftDiskMap(fftCache);
    VlStFtExt ext = new VlStFtExt(new VlFft(rp.fftBufferSize, rp.fftDirect), rp.fftHopSize, fm);

    ext.reset(new VlSampleExt(audioSrc, rp.audioScaleToUnit));
    System.out.printf("Extracted [%d] fft samples%n", ext.asStream().count());
    ext.reset(new VlSampleExt(audioSrc, rp.audioScaleToUnit));

    float[][] spec = ext.asStream().map(smp -> smp.realQtr).toArray(float[][]::new);
    FileWriter fw = new FileWriter(spectrumOut);
    g.toJson(spec, fw);
    fw.close();
    fm.close();
  }

  public static void loadPatches(float[][] spectrum, List<VlTonePatch> patches) {
    for (VlTonePatch p : patches) {
      p.spectrum = new float[rp.regionWidth][rp.regionHeight];
      regionSquare(spectrum, p.x, p.y, p.spectrum);
    }
  }

  public static void main(String[] args) throws Exception {
    URL pgUrl = VlNetTrainer.class.getResource("/patch-group.json");
    Reader pgr = new InputStreamReader(Objects.requireNonNull(pgUrl).openStream());
    VlPatchGroup pg = g.fromJson(pgr, VlPatchGroup.class);

    System.out.println("================ Reference patches ================");
    System.out.printf("Straight: [%d]%n", pg.sources.stream().flatMap(ps -> ps.patches.stream()).filter(p -> p.toneType == VlToneType.STRAIGHT).count());
    System.out.printf("Shift:    [%d]%n", pg.sources.stream().flatMap(ps -> ps.patches.stream()).filter(p -> p.toneType == VlToneType.SHIFT).count());

    for (VlPatchSource src : pg.sources) {
      URL audioUrl = Objects.requireNonNull(VlNetTrainer.class.getResource(src.audioUrl));
      File spectrumFile = new File("./build", String.format("%s.fft.json", Paths.get(src.audioUrl).getFileName()));
      if (!spectrumFile.exists()) {
        dumpSpectrum(audioUrl, spectrumFile);
      }
      float[][] spec = g.fromJson(new FileReader(spectrumFile), float[][].class);
      loadPatches(spec, src.patches);
    }

    File patchGroupFile = new File("./build", "patch-group.json");
    FileWriter fw = new FileWriter(patchGroupFile);
    g.toJson(pg, fw);
    fw.close();

    JtActivationFn actFn = new JtSigmoid();
    JtErrorFn errFn = new JtMeanSquaredError();
    JtNetwork net = new JtNetwork().init(
        rp.regionWidth * rp.regionHeight,
        new JtRandomInitializer().init(154866485),
        new JtSgdUpdater().init(1.0, 1.0),
        new JtLayer().init(64, actFn),
        new JtOutputLayer().init(VlToneType.values().length, actFn, errFn)
    );

    JtTrainer trainer = new JtTrainer(net, (n, epoch, error) -> {
      System.out.printf("Epoch: [%s], error: [%.9f]%n", epoch, error);
      return error != -1 && error < 1 - rp.netTrainError;
    }, new VlPatchSupplier(pg, rp));

    File out = new File("./src/main/resources/io/vacco/stft/net.ser");

    trainer.start();
    JtIo.writeNet(net, new FileOutputStream(out));
    System.out.println("done");
  }

}

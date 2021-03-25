package io.vacco.volach.fprint.pk;

import com.esotericsoftware.yamlbeans.YamlWriter;
import io.vacco.jtinn.activation.*;
import io.vacco.jtinn.error.*;
import io.vacco.jtinn.net.*;
import io.vacco.volach.fprint.pk.dto.VlPeakType;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static io.vacco.volach.fprint.pk.dto.VlAnalysisRegion.*;
import static io.vacco.volach.VlAnalysisUtil.*;

public class VlPeakTrainingTask {

  public static void main(String[] args) throws IOException {
    JtActivationFn actFn = new JtSigmoid();
    JtErrorFn errFn = new JtMeanSquaredError();
    JtNetwork net = new JtNetwork().init(
        RegionSize * RegionSize, new JtRandomInitializer().init(154866485),
        new JtSgdUpdater().init(1.0, 1.0),
        new JtLayer().init(64, actFn),
        new JtOutputLayer().init(VlPeakType.values()[0].flags.length, actFn, errFn)
    );

    JtTrainer trainer = new JtTrainer(net, (n, epoch, error) -> {
      System.out.printf("Epoch: [%s], error: [%.6f]%n", epoch, error);
      return error != -1 && error < 0.0002;
    }, new VlAnchorTrainingSupplier(mapper, new File("./vl-fprint-pk/peak-training/peaks.json")));

    File out = new File("./vl-fprint-pk/src/main/resources/io/vacco/volach/fprint/pk/net.yml");
    YamlWriter w = new YamlWriter(new FileWriter(out));

    trainer.start();
    w.write(net);
    w.close();
    System.out.println("done");
  }

}

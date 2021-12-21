package io.vacco.volach.fprint.pk.net;

import io.vacco.jtinn.activation.*;
import io.vacco.jtinn.error.*;
import io.vacco.jtinn.net.*;
import io.vacco.jtinn.util.JtIo;
import io.vacco.volach.fprint.pk.dto.VlAnalysisParameters;
import io.vacco.volach.fprint.pk.dto.VlPeakType;

import java.io.*;

import static io.vacco.volach.fprint.pk.dto.VlAnalysisParameters.*;
import static io.vacco.volach.VlAnalysisUtil.*;

public class VlNet02TrainTask {

  public static void main(String[] args) throws IOException {
    VlAnalysisParameters analysisParams = forReference();
    JtActivationFn actFn = new JtSigmoid();
    JtErrorFn errFn = new JtMeanSquaredError();
    JtNetwork net = new JtNetwork().init(
        analysisParams.netRegionSize * analysisParams.netRegionSize,
        new JtRandomInitializer().init(154866485),
        new JtSgdUpdater().init(1.0, 1.0),
        new JtLayer().init(64, actFn),
        new JtOutputLayer().init(VlPeakType.values()[0].flags.length, actFn, errFn)
    );

    JtTrainer trainer = new JtTrainer(net, (n, epoch, error) -> {
      System.out.printf("Epoch: [%s], error: [%.9f]%n", epoch, error);
      return error != -1 && error < 0.0002;
    }, new VlAnchorTrainingSupplier(json, new File("./vl-fprint-pk/peak-training/peaks.json")));

    File out = new File("./vl-fprint-pk/src/main/resources/io/vacco/volach/fprint/pk/net.ser");

    trainer.start();
    JtIo.writeNet(net, new FileOutputStream(out));
    System.out.println("done");
  }

}

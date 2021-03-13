package io.vacco.volach.fprint;

import io.vacco.jtinn.activation.JtActivationFn;
import io.vacco.jtinn.activation.JtSigmoid;
import io.vacco.jtinn.error.JtErrorFn;
import io.vacco.jtinn.error.JtMeanSquaredError;
import io.vacco.jtinn.net.*;

public class VlNnFpProcessor {

  public final JtNetwork net;
  public final JtTrainer trainer;

  private double errSum = 0, errAvg = 0;

  public VlNnFpProcessor(VlNnFpSampleSupplier sampleSupplier) {
    JtActivationFn actFn = new JtSigmoid();
    JtErrorFn errFn = new JtMeanSquaredError();
    this.net = new JtNetwork().init(
        sampleSupplier.getInputSize(),
        new JtRandomInitializer().init(12345),
        new JtSgdUpdater().init(0.5, 1), // TODO move these init params somewhere else.
        // new JtLayer().init(512, actFn),
        new JtLayer().init(256, actFn),
        new JtOutputLayer().init(64, actFn, errFn)
    );

    this.trainer = new JtTrainer(this.net, (net, epoch, error) -> {
      errSum += error;
      errAvg = errSum / (epoch + 1);
      System.out.printf("Epoch [%s], error: [%s], avg: [%s]%n", epoch, error, errAvg);
      return epoch == 10;
      // return error != -1 && error < 0.0001;
    }, sampleSupplier);
  }

}

package io.vacco.volach.fprint.pk;

import io.vacco.volach.wavelet.VlWpNode;
import io.vacco.volach.wavelet.dto.VlAnalysisParameters;
import io.vacco.volach.wavelet.type.VlBattle23;

public class VlFprintSpecUtil {

  public static final int trainingRegionSize = 32;
  public static final int cutoffFreqBands = 128;

  public static final String[][] sources = {
      {"/Users/jjzazuet/Desktop/sample-001.mp3", "./vl-fprint-pk/build/sample-001.mp3-spectrum.json", "./vl-fprint-pk/build/sample-001.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-002.mp3", "./vl-fprint-pk/build/sample-002.mp3-spectrum.json", "./vl-fprint-pk/build/sample-002.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-003.mp3", "./vl-fprint-pk/build/sample-003.mp3-spectrum.json", "./vl-fprint-pk/build/sample-003.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-004.mp3", "./vl-fprint-pk/build/sample-004.mp3-spectrum.json", "./vl-fprint-pk/build/sample-004.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-005.mp3", "./vl-fprint-pk/build/sample-005.mp3-spectrum.json", "./vl-fprint-pk/build/sample-005.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-006.mp3", "./vl-fprint-pk/build/sample-006.mp3-spectrum.json", "./vl-fprint-pk/build/sample-006.mp3-spectrum.csv"},
      {"/Users/jjzazuet/Desktop/sample-007.mp3", "./vl-fprint-pk/build/sample-007.mp3-spectrum.json", "./vl-fprint-pk/build/sample-007.mp3-spectrum.csv"}
  };

  public static final VlAnalysisParameters trainingParams = VlAnalysisParameters.from(
      null, 131_071, 9, true,
      new VlBattle23(), VlWpNode.Order.Sequency
  );

}

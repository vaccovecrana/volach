package io.vacco.volach.impl;

import io.vacco.volach.extraction.*;
import io.vacco.volach.schema.*;

import java.io.Serializable;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class VlAudioMatch {

  public static void indexTrack(long trackId, List<VlFftPeakPair> pairs, VlFingerPrintIndex db) {
    for (VlFftPeakPair pp : pairs) {
      pp.pk0.withTrackId(trackId);
      pp.pk1.withTrackId(trackId);
      db.computeIfAbsent(pp.fftFreqId(), fftK -> new ArrayList<>()).add(pp);
    }
  }

  public static List<VlFftPeakPair> fingerPrint(URL audioSrc, VlStFtParams exp, Map<Integer, VlFftSample> fftCache) {
    VlStFtExt ext = new VlStFtExt(new VlFft(exp.fftBufferSize, exp.fftDirect), exp.fftHopSize, fftCache);
    VlRegionExt rxt = new VlRegionExt(audioSrc, ext, exp);
    return rxt.peakPairs(exp.peakDistanceMin, exp.peakDistanceMax);
  }

  public static Map<Long, Map<String, VlMatchPeak>> align(List<VlFftPeakPair> smpPairs, VlFingerPrintIndex database, int topMatchThreshold) {
    Map<Long, Map<String, VlMatchPeak>> diffIdx = new TreeMap<>();
    for (VlFftPeakPair p : smpPairs) {
      List<VlFftPeakPair> dbMatches = database.get(p.fftFreqId());
      if (dbMatches != null) {
        for (VlFftPeakPair dp : dbMatches) {
          long dt0 = dp.pk0.fftOff - p.pk0.fftOff;
          long dt1 = dp.pk1.fftOff - p.pk1.fftOff;
          VlMatchPeak mp0 = VlMatchPeak.from(p.pk0, dp.pk0);
          VlMatchPeak mp1 = VlMatchPeak.from(p.pk1, dp.pk1);
          diffIdx.computeIfAbsent(dt0, diff -> new TreeMap<>()).put(dp.pk0.fftKey(), mp0);
          diffIdx.computeIfAbsent(dt1, diff -> new TreeMap<>()).put(dp.pk1.fftKey(), mp1);
        }
      }
    }
    return new LinkedHashMap<>(
        diffIdx.entrySet().stream()
            .sorted((e0, e1) -> e1.getValue().size() - e0.getValue().size())
            .limit(topMatchThreshold)
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
    );
  }

  public static List<VlMatchRange> rangesOf(Map<Long, Map<String, VlMatchPeak>> fpMatches, int trackId, long queryLengthSmp) {
    List<VlMatchRange> out = new ArrayList<>();
    for (Map<String, VlMatchPeak> pkIdx : fpMatches.values()) {
      List<VlMatchPeak> peaks = pkIdx.values().stream()
          .filter(pmk -> pmk.source.trackId == trackId)
          .sorted(Comparator.comparingLong(pmk -> pmk.source.smp))
          .collect(Collectors.toList());

      VlMatchPeak mpk0 = peaks.get(0);
      VlMatchPeak mpk1 = peaks.get(peaks.size() - 1);

      long matchLen = mpk1.source.smp - mpk0.source.smp;
      long smpFill = queryLengthSmp - matchLen;
      if (smpFill > 0) {
        long smpStart = mpk0.source.smp - mpk0.query.smp;
        long smpEnd = smpStart + queryLengthSmp;
        out.add(VlMatchRange.from(smpStart, smpEnd, peaks.size()));
      }
    }
    return out;
  }

  public static long sampleLengthOf(URL audioSrc) {
    return new VlSampleExt(audioSrc, false).asStream().count();
  }

}

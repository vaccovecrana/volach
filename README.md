# volach

[volach](https://en.wikipedia.org/wiki/Valac) is a minimal audio fingerprinting library.

## References

- [PyWavelets](https://github.com/PyWavelets/pywt)
- [MaryTTS](https://github.com/marytts/marytts)
- [JWave](https://github.com/graetz23/JWave)
- [Walden, A. T., and A. Contreras Cristan. “The Phase-Corrected Undecimated Discrete Wavelet Packet Transform and Its Application to Interpreting the Timing of Events.” Proceedings: Mathematical, Physical and Engineering Sciences, vol. 454, no. 1976, 1998, pp. 2243–2266. JSTOR, www.jstor.org/stable/53216. Accessed 7 Feb. 2021.](https://www.jstor.org/stable/53216?seq=1)

## Notes

For a 2^N input signal array, the transform at a certain level will yield analysis for at most
some lowest power of two in terms of wavelet packet samples, so the audio signal extractor must
account for that as well.

For the Haar1 wavelet:
```
512smp @ lv1 -> 256 smp,   2   freq bands
512smp @ lv4 -> 32  smp,   16  freq bands
512smp @ lv8 -> 2   smp,   256 freq bands
512smp @ lv9 -> undefined
```

For the Daubechies4 wavelet:
```
512smp @ lv1 -> 256 smp,   2   freq bands
512smp @ lv4 -> 32  smp,   16  freq bands
512smp @ lv5 -> 16  smp,   32  freq bands
512smp @ lv6 -> 8   smp,   64  freq bands
512smp @ lv7 -> undefined
```

For the Daubechies16 wavelet:
```
512smp @ lv1 -> 256 smp,   2   freq bands
512smp @ lv4 -> 32  smp,   16  freq bands
512smp @ lv5 -> undefined
512smp @ lv6 -> undefined
```

The `motherWavelength` attribute of the wavelet describes the amount of samples
that can be decomposed for some amount of input data.

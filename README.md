# volach

[volach](https://en.wikipedia.org/wiki/Valac) is a minimal audio fingerprinting library.

FFT notes:

- Audio data comes in chunks of 512 as `float[]`.
- Audio sample values need to be normalized.  
- FFT data is the same size as input audio values.

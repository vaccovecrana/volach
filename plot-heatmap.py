import matplotlib.pyplot as plt
import numpy as np

f = './build/stft-audio.csv'
# f = './build/stft-samples.csv'
# f = './build/stft-region.csv'

matrix = np.loadtxt(f, delimiter=',')
# matrix = np.rot90(matrix) # when choosing patch regions, invert the X/Y axis coordinates to match FFT array dimensions.

fig, ax = plt.subplots()
im = plt.imshow(
    matrix, cmap='bone', interpolation='none',
    origin='upper', aspect='auto',
    vmin=0.0, vmax=0.1
)
plt.show()

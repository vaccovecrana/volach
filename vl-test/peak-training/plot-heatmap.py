import matplotlib.pyplot as plt
import numpy as np
import mplcursors


class Formatter(object):
    def __init__(self, im):
        self.im = im

    def __call__(self, x, y):
        z = self.im.get_array()[int(y), int(x)]
        return 'x=%i, y=%i, z=%.4f' % (x, 127 - y, z)


f = '../build/sample-002.mp3-spectrum.csv'
# f = '../build/coefficients-eas-l4-frequency.txt'
# f = '../build/coefficients-linchirp-l4-frequency.txt'

matrix = np.loadtxt(f, delimiter=',')
matrix = np.rot90(matrix)

fig, ax = plt.subplots()
im = plt.imshow(
  matrix, cmap='viridis', interpolation='bilinear',
  origin='upper', aspect='auto',
  vmin=0.0, vmax=0.03
)
ax.format_coord = Formatter(im)
cursor = mplcursors.cursor(hover=True)
plt.show()

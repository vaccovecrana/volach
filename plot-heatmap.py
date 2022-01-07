import matplotlib.pyplot as plt
import numpy as np


class Formatter(object):
    def __init__(self, im):
        self.im = im

    def __call__(self, x, y):
        z = self.im.get_array()[int(y), int(x)]
        return 'x=%i, y=%i/%i, z=%.9f' % (x, 127 - y, y, z)

# f = '../build/extraction-anc.csv'
# f = '../build/extraction-spec.csv'
# f = '../build/sample-004.mp3-spectrum.csv'

f = './vl-core/build/coefficients-eas-l4-frequency.txt'
# f = './vl-core/build/coefficients-eas-sample-l4-natural.txt'
# f = './vl-core/build/coefficients-linchirp-l4-frequency.txt'

matrix = np.loadtxt(f, delimiter=',')
matrix = np.rot90(matrix)

fig, ax = plt.subplots()
im = plt.imshow(
    matrix, cmap='viridis', interpolation='none',
    origin='upper', aspect='auto',
    # vmin=0.0, vmax=0.06
    # vmin=-0.31591344, vmax=0.3958072 - chirp signal
    vmin=0.0, vmax=0.027650684 # eas sample
)
ax.format_coord = Formatter(im)

plt.show()

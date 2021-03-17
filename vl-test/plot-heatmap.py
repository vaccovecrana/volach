import matplotlib.pyplot as plt
import numpy as np

# f = './build/coefficients-eas-l4-frequency.txt'
# f = './build/coefficients-linchirp-l4-frequency.txt'
f = './build/coefficient-peaks-eas.txt'

matrix = np.loadtxt(f, delimiter=',')
matrix = np.rot90(matrix)

plt.imshow(
    matrix, cmap='viridis', interpolation='nearest',
    origin='upper', aspect='auto',
    vmin=0.0, vmax=981.23206
)
plt.show()

# Quantum Computing & Mechanics

Quantum computing exploits the fundamental principles of quantum mechanics—namely **superposition**, **entanglement**, and **interference**—to solve computational problems beyond the reach of classical supercomputers.

---

## 1. The Qubit State Vector

Unlike a classical bit which is strictly deterministic ($0$ or $1$), a qubit exists in a normalized linear superposition in a two-dimensional complex Hilbert space $\mathcal{H}_2$:

$$
|\psi\rangle = \alpha |0\rangle + \beta |1\rangle \quad \text{where } \alpha, \beta \in \mathbb{C}
$$

The normalization axiom requires that total probability equals unity:

$$
|\alpha|^2 + |\beta|^2 = 1
$$

In spherical coordinates, this state is mapped onto the **Bloch Sphere**:

$$
|\psi\rangle = \cos\left(\frac{\theta}{2}\right)|0\rangle + e^{i\phi}\sin\left(\frac{\theta}{2}\right)|1\rangle
$$

where $0 \le \theta \le \pi$ and $0 \le \phi < 2\pi$.

---

## 2. Fundamental Dynamics: The Schrödinger Equation

The time evolution of a closed quantum system is governed by the time-dependent Schrödinger equation:

$$
i\hbar \frac{d}{dt} |\psi(t)\rangle = \hat{\mathcal{H}} |\psi(t)\rangle
$$

For a time-independent Hamiltonian $\hat{\mathcal{H}}$, the state evolves via the unitary operator:

$$
\hat{U}(t) = \exp\left(-\frac{i}{\hbar} \hat{\mathcal{H}} t\right)
$$

---

## 3. Quantum Entanglement & Bell States

Entanglement represents a non-local correlation between multiple qubits. The four maximally entangled **Bell States** form an orthonormal basis:

$$
|\Phi^+\rangle = \frac{|00\rangle + |11\rangle}{\sqrt{2}}, \quad |\Phi^-\rangle = \frac{|00\rangle - |11\rangle}{\sqrt{2}}
$$

$$
|\Psi^+\rangle = \frac{|01\rangle + |10\rangle}{\sqrt{2}}, \quad |\Psi^-\rangle = \frac{|01\rangle - |10\rangle}{\sqrt{2}}
$$

### Pauli Operators

The three orthogonal Pauli spin matrices are defined as:

$$
\sigma_x = \begin{pmatrix} 0 & 1 \\ 1 & 0 \end{pmatrix}, \quad
\sigma_y = \begin{pmatrix} 0 & -i \\ i & 0 \end{pmatrix}, \quad
\sigma_z = \begin{pmatrix} 1 & 0 \\ 0 & -1 \end{pmatrix}
$$

They satisfy the algebraic commutation relation:

$$
[\sigma_j, \sigma_k] = 2i \sum_{l=1}^3 \epsilon_{jkl} \sigma_l
$$

---

## 4. Key Properties Table

| Gate | Unitary Matrix | Function |
| :--- | :--- | :--- |
| Hadamard ($H$) | $\frac{1}{\sqrt{2}}\begin{pmatrix} 1 & 1 \\ 1 & -1 \end{pmatrix}$ | Creates equal superposition |
| Phase ($S$) | $\begin{pmatrix} 1 & 0 \\ 0 & i \end{pmatrix}$ | $\pi/2$ phase rotation around Z-axis |
| CNOT | $4 \times 4$ Permutation | Two-qubit conditional entanglement |

> "Nature isn't classical, dammit, and if you want to make a simulation of nature, you'd better make it quantum mechanical."  
> — *Richard Feynman*

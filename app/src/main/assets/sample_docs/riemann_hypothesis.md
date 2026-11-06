# The Riemann Zeta Function & Prime Distribution

The Riemann zeta function is the cornerstone of modern analytic number theory, encoding deep secrets about the distribution of prime numbers.

---

## 1. Definition and Euler Product

For complex numbers $s = \sigma + it$ with real part $\sigma > 1$, the zeta function is defined by the Dirichlet series:

$$
\zeta(s) = \sum_{n=1}^\infty \frac{1}{n^s}
$$

Leonhard Euler established the profound link between primes and analysis via the infinite Euler product:

$$
\zeta(s) = \prod_{p \in \mathbb{P}} \frac{1}{1 - p^{-s}} \quad (\text{for } \text{Re}(s) > 1)
$$

---

## 2. Analytic Continuation & Functional Equation

Bernhard Riemann proved that $\zeta(s)$ extends to a meromorphic function over the entire complex plane $\mathbb{C}$, with its only pole being a simple pole at $s = 1$ with residue $1$:

$$
\lim_{s \to 1} (s - 1) \zeta(s) = 1
$$

It satisfies the symmetric reflection functional equation:

$$
\pi^{-\frac{s}{2}} \Gamma\left(\frac{s}{2}\right) \zeta(s) = \pi^{-\frac{1-s}{2}} \Gamma\left(\frac{1-s}{2}\right) \zeta(1-s)
$$

---

## 3. The Riemann Hypothesis

The trivial zeros of $\zeta(s)$ lie at negative even integers:

$$
s \in \{-2, -4, -6, -8, \dots\}
$$

All non-trivial zeros lie within the **critical strip**:

$$
0 < \text{Re}(s) < 1
$$

> **The Riemann Hypothesis (1859):**  
> Every non-trivial zero of $\zeta(s)$ lies precisely on the critical line:
> $$
> \text{Re}(s) = \frac{1}{2}
> $$

---

## 4. Connection to Prime Counting Function

Let $\pi(x)$ denote the number of primes less than or equal to $x$. Riemann's explicit formula demonstrates that fluctuations in prime density are determined by the imaginary parts $\gamma_k$ of the zeros:

$$
\psi(x) = x - \sum_{\rho: \zeta(\rho)=0} \frac{x^\rho}{\rho} - \ln(2\pi) - \frac{1}{2}\ln(1 - x^{-2})
$$

Where $\rho = \frac{1}{2} + i\gamma$ represents the non-trivial zeros.

# Material 3 You: Expressive Motion & Dynamics

Material Design 3 (Material You) bridges personal expression and scientific motion physics to build human-centric digital interfaces.

---

## 1. Physics of Expressive Springs

Natural interactions are modeled as damped harmonic oscillators:

$$
m \frac{d^2 x}{dt^2} + c \frac{dx}{dt} + k x = 0
$$

Where:
- $m$: Mass of the interface surface
- $c$: Damping coefficient ($c = 2\sqrt{m k}\cdot\zeta$)
- $k$: Spring stiffness constant
- $\zeta$: Damping ratio ($\zeta < 1$ produces expressive, pleasant bounce)

---

## 2. Dynamic Monet Color Space

Color extraction samples the dominant 128 hues from the wallpaper bitmap, projects them into the perceptual **CAM16** color space, and constructs 5 key tonal palettes:

- **Primary**: Dominant thematic tone
- **Secondary**: Balanced structural contrast
- **Tertiary**: Playful highlight accents
- **Neutral**: Background and surface containers
- **Neutral Variant**: Borders, dividers, and subtle outlines

---

## 3. Reader Feature Checklist

- [x] **Rust Core**: Blazing fast AST extraction & regex parsing
- [x] **KaTeX Math Engine**: Crisp rendering of inline and block equations
- [x] **Material 3 You**: Adaptive Dynamic Colors & Custom Warm Themes
- [x] **Expressive Motion**: Tactile page curl, bouncy sheet dialogs
- [x] **SAF Document Import**: Open any `.md` file from storage

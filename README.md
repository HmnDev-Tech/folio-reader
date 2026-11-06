# Folio Reader

A manuscript-grade Markdown and LaTeX reader for Android, built with **Kotlin**, **Jetpack Compose (Material 3 You)**, and a **Rust Native Engine**.

---

## Highlights

- **Native Rust Engine (`rust-core`)**: Blazing-fast document analysis, heading hierarchy extraction, AST tokenization, formula cataloging, and reading time computation via JNI.
- **LaTeX Math Support**: Sharp, hardware-accelerated rendering of inline (`$..$`) and display (`$$..$$`) mathematical equations using KaTeX.
- **Material 3 You Tactile Aesthetics**:
  - Full Android 12+ dynamic wallpaper color extraction (Monet).
  - Built-in tactile atmospheres: *Velum Parchment*, *Obsidian Manuscript*, *Sepia Solace*, and *Pure AMOLED*.
  - Spring-physics motion (`Spring.DampingRatioLowBouncy`).
- **Two Reading Modes**:
  - **Tactile Pager Mode**: 3D paper tilt and scale animations simulating heavyweight book pages.
  - **Continuous Scroll Mode**: Fluid vertical reading with dynamic progress pill and auto-collapsing top bar.
- **Expressive Bottom Sheets**:
  - **Table of Contents**: Indented heading navigation.
  - **LaTeX Formula Inspector**: Browse all equations in the document with one-tap LaTeX source copy.
  - **Typography Controls**: Real-time adjustment of font sizes, line spacing, and typeface families (Serif, Sans, Mono).
  - **Atmosphere & Theme Selector**: Instant palette preview and selection.
- **SAF Document Import**: Open any `.md`, `.markdown`, or `.txt` file directly from device storage or file managers.
- **Prepackaged Showcase Documents**: Includes papers on Quantum Mechanics, Riemann Hypothesis, and Material 3 Dynamics to explore right away.

---

## Architecture

```
folio-reader/
├── rust-core/                  # High-performance Rust parser crate
│   ├── Cargo.toml
│   └── src/lib.rs             # Regex AST engine & JNI bridge
├── app/
│   ├── src/main/java/com/hmndev/folioreader/
│   │   ├── core/              # Rust JNI bridge & document model
│   │   ├── ui/
│   │   │   ├── components/    # Reusable Material 3 components & sheets
│   │   │   ├── screens/       # Shelf & Reader screens
│   │   │   ├── theme/         # Color palettes, typography & spring motion
│   │   │   └── viewmodel/     # State management & document loader
│   │   └── assets/            # KaTeX engine & sample documents
└── .github/workflows/         # Automated APK builder in GitHub Actions
    └── build-apk.yml
```

---

## CI / Automated APK Builder

The GitHub Actions workflow (`.github/workflows/build-apk.yml`) automatically builds the native Rust `.so` libraries for Android (`arm64-v8a`, `x86_64`), runs the test suites, compiles the APK, and uploads it to GitHub Artifacts.

---

## Design Discipline

See [TASTE.md](TASTE.md) for the aesthetic principles and anti-patterns guiding this project.

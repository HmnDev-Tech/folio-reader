# TASTE · Folio Reader Aesthetic Architecture

## Core Philosophy
Folio is not a plain Markdown viewer. It is **a living digital manuscript with tactile elegance**.
Reading mathematical formulas and structured documents should feel as calming, focused, and tactile as turning pages of a fine editorial journal printed on heavyweight parchment, infused with the fluid intelligence of Material 3 You.

## Color Palette & Atmospheres
- **Velum Parchment (Default Light)**:
  - Surface: `Warm Cream #FDFBF7`
  - OnSurface: `Deep Ink #191C1E`
  - Primary Accent: `Terracotta Amber #C2673D` (tactile, organic, inviting)
  - Formula Highlight: `Warm Umber #78350F`
  - Code Block Surface: `Parchment Shade #F3EFE6`
- **Obsidian Manuscript (Dark)**:
  - Surface: `Midnight Slate #0F1216`
  - OnSurface: `Crisp Starlight #E2E8F0`
  - Primary Accent: `Electric Mint #2DD4BF`
  - Formula Highlight: `Cyan Tint #38BDF8`
  - Code Block Surface: `Deep Abyss #181E26`
- **Sepia Solace**:
  - Warm golden hour palette for low-eyestrain reading.
- **Pure AMOLED**:
  - Pitch black `#000000` surface with high-contrast soft typography.
- **Material 3 Dynamic You**:
  - Automatically derives expressive tonal palettes from the user's Android wallpaper.

## Tactile Motion & Transitions
- **Expressive Spring Physics**:
  - Controls, dialogs, and sheets react with bouncy, organic spring motion (`Spring.DampingRatioLowBouncy`, `Spring.StiffnessMediumLow`).
- **Tactile Pager Flip**:
  - In *Book / Folio Paging Mode*, pages don't merely slide: they subtly scale (`0.94f`), gently dim, and reveal soft drop shadows as if physical high-gsm paper is pivoting over.
- **Floating Pill Dock**:
  - The reader toolbar floats like a polished capsule at the bottom. Tapping the document gently fades and slides it away into true immersive reading mode.
- **Interactive Formula Inspector**:
  - Tapping mathematical expressions opens an expressive Material 3 modal bottom sheet with formula analysis, LaTeX source syntax copy, and formula complexity breakdown powered by Rust.

## Typography
- **Reading Body**: Balanced Serif and Modern Sans with generous x-height, warm contrast, and customizable line-height (1.3x to 2.0x).
- **Mathematical Equations**: Rendered with KaTeX mathematical typesetting precision, sharp vector rendering at all zoom scales.
- **Monospace Snippets**: JetBrains-style code font with rounded syntax highlighting blocks.

## Explicit Anti-Patterns
- Never raw unstyled markdown dumps without margins and typographic rhythm.
- Never rigid, instant zero-animation screen switching.
- Never tiny unreadable formula fragments; formulas must be clearly rendered with pinch-to-inspect capability.

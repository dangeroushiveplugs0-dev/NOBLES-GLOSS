# NOBLES-GLOSS

**NOBLES-GLOSS** is an independent, mobile-first 3D creation application with an original visual identity inspired by Alcina Dimitrescu.

- **Working branch:** `DIMITTRESCU-SHEEN`
- **Interface system:** DIMITTRESCUI
- **Goal:** editable 3D creation, an expansive procedural PBR material library, and progressively deeper professional workflows.
- **SomaS3D relationship:** reference only. ShofterUI code may be studied for reusable ideas after reviewing exact files and licenses. This project does not inherit SomaS3D's product identity or architecture by default.

## Principles

1. Viewport-first layout that adapts to phones and tablets.
2. Touch-friendly controls, responsive panels, and correct system-bar insets.
3. Editable scene and material data, not just flattened images.
4. Modular boundaries between UI, geometry, rendering, materials, import/export, and platform integration.
5. Progressive, testable milestones rather than claiming full Blender parity prematurely.
6. Verify upstream maintenance, licenses, mobile support, and performance before adopting dependencies.
7. Treat imported projects as untrusted data; never automatically execute embedded scripts or expressions.

## Documentation

- [Architecture](docs/ARCHITECTURE.md)
- [Technology evaluation](docs/TECHNOLOGY-EVALUATION.md)
- [Roadmap](docs/ROADMAP.md)
- [DIMITTRESCUI design principles](docs/DIMITTRESCUI.md)

## Current implementation

The first Android scaffold provides the DIMITTRESCUI visual direction and a viewport placeholder. It is not yet a functioning 3D renderer or modeling engine. CI builds a debug APK so the shell can be installed and evaluated on a phone.

## Build

The GitHub Actions workflow installs Gradle and the Android SDK, runs Android unit tests, builds the debug APK, and uploads it as an artifact. No Android Studio installation is required on the developer's phone.

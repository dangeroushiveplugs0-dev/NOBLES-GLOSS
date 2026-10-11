# Technology evaluation

**Status: shortlist, not final dependency approval.** Re-check licenses, release health, platform support, build size and maintenance when integrating.

| Area | Candidate | Why investigate | Risks to validate |
|---|---|---|---|
| Native real-time PBR renderer | [Google Filament](https://github.com/google/filament) | Real-time physically based rendering and mobile-oriented support | Native lifecycle complexity, device/API coverage, advanced material requirements, license notices |
| Web rendering alternative | [three.js](https://github.com/mrdoob/three.js) | Large ecosystem and glTF support; useful for fast prototyping | WebView/GPU memory cost, WebGPU support variation, no editable topology core |
| Geometry | [Rust halfedge crate](https://docs.rs/halfedge/latest/halfedge/) | Topology operations, subdivision, decimation, parameterization and deformation | Newer ecosystem; validate Android/iOS compilation, API stability, memory use and algorithm correctness |
| Geometry alternative | [Alum](https://github.com/ranjeethmahankali/alum) | Rust half-edge mesh library; repository lists BSD-3-Clause | Feature coverage and mobile integration require a proof of concept |
| Procedural material reference | [Material Maker](https://www.materialmaker.org/) | Node-based PBR authoring, broad node catalog and community material library | Verify code and every asset's license; desktop-oriented pieces may not transfer to mobile |
| Engine alternative | [Godot license](https://godotengine.org/license/) | MIT-licensed engine and mature cross-platform ecosystem | A full embedded engine may constrain custom DCC architecture; inspect editor embedding and mobile limits |
| Interchange | [glTF](https://www.khronos.org/gltf/) | Portable mesh, PBR material, skin and animation exchange | Not a replacement for a richer editable native scene format |

## Recommendation
Prototype an Android application shell with a renderer adapter and independent scene/geometry model before committing to a permanent engine. Filament is the leading native PBR renderer to test; a Rust half-edge library is a candidate, not an adopted dependency. Keep DIMITTRESCUI independent from both.

Before adopting any stack, measure startup, memory, rendering, model upload cost, shader flexibility, binary size, CI complexity and license obligations on representative phones. Do not commit to a full Rust/C++ rewrite or a web-only architecture before the comparison.

## Material-system strategy
Build an original editable material graph and curated catalog. Existing tools can inform workflow, but do not copy community content until its individual license is known.

Initial categories:
- **Wetness:** water films, rain-soaked surfaces, droplets, condensation, streaks, wet/dry masks and puddled roughness.
- **Organic:** pores, wax, membranes, fungi, moss, biological patterns and subsurface-scattering experiments.
- **Aging:** rust, oxidation, chipped paint, grime, scratches, cracks, dust and layered deposits.
- **Optical:** clear coat, pearlescence, iridescence, thin-film coloration, translucent plastics, emissive layers and stylized holographic effects.
- **Ornate/stylized:** lacquer, metallic trim, enamel, porcelain, velvet-like approximations and decorative masks.

These are graph templates, not a promise that every effect is physically accurate on every phone. Use quality tiers and clearly mark effects requiring baking or advanced rendering.

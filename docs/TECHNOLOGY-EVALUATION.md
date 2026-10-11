# Technology candidates

Candidates only. Verify current license, maintenance, Android/iOS support, binary size and performance before adoption.

| Area | Candidate | Why investigate | Validate |
|---|---|---|---|
| Native PBR renderer | [Google Filament](https://github.com/google/filament) | Real-time physically based rendering and mobile support | Native lifecycle, device coverage, shader needs, notices |
| Rendering alternative | [three.js](https://github.com/mrdoob/three.js) | Broad ecosystem and glTF support | WebView/GPU memory; not an editable mesh core |
| Geometry | [Rust halfedge](https://docs.rs/halfedge/latest/halfedge/) | Topology, subdivision, decimation and parameterization | API stability, mobile compilation, memory, tests |
| Geometry alternative | [Alum](https://github.com/ranjeethmahankali/alum) | Rust half-edge mesh | Feature coverage and mobile integration |
| Material reference | [Material Maker](https://www.materialmaker.org/) | Node-based procedural PBR authoring | Check code and individual asset licenses |
| Engine alternative | [Godot license](https://godotengine.org/license/) | MIT-licensed cross-platform engine | Editor embedding and architecture constraints |
| Interchange | [glTF](https://www.khronos.org/gltf/) | Meshes, PBR, skins and animation | Not a native editable scene format |

Recommendation: prototype Android shell + renderer adapter + independent scene/geometry model. Filament is the first native renderer to test, not an adopted dependency. Compare startup, memory, shader flexibility, upload cost, package size and CI complexity.

## Material strategy
Build original editable recipes, checking provenance/licenses:
- Wetness: rain-soaked surfaces, water films, droplets, condensation, streaks, wet/dry masks.
- Organic: pores, wax, membranes, fungi, moss, biological patterns and subsurface experiments.
- Aging: rust, oxidation, chipped paint, grime, scratches, cracks and dust.
- Optical: clear coat, pearlescence, iridescence, thin-film color, translucent plastics and emission.
- Ornate/stylized: lacquer, metallic trim, enamel, porcelain, velvet-like approximations and decorative masks.

Use quality tiers and mark effects that require baking or higher-end rendering.

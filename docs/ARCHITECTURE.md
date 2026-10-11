# Architecture

## Subsystems
- DIMITTRESCUI: navigation, panels, gestures, inspectors and node editors.
- Session/commands: selection, tool state, undo/redo and transactions.
- Scene model: stable IDs, hierarchy, transforms and versioned serialization.
- Geometry: editable vertices/edges/faces, topology operations, UVs, attributes, modifiers and sculpt data.
- Material graph: typed nodes/sockets, parameters, validation, procedural evaluation, shader compilation and baking.
- Renderer adapter: viewport, camera, lighting, GPU resources and quality tiers.
- Import/export: format-specific adapters and curated fixture tests.
- Animation/rigging: skeletons, weights, morphs, IK and timelines.
- Physics: isolated solvers, caches, cancellation and preview quality.
- Project/assets: persistence, recovery, metadata and license provenance.

## Invariants
Scene/mesh data are authoritative; GPU resources are derived. Material graphs are authoritative; shaders and baked maps are regenerable caches. UI state is not the only project storage. Edits should use undoable commands. Importers validate indices, paths, sizes and resource budgets. Imported scripts never execute automatically. Heavy jobs run off the UI thread and report progress.

## Materials
Use a versioned renderer-independent graph with stable IDs, typed sockets and serializable parameters. Initial channels: base color, metallic, roughness, normal, height, opacity, emission, AO, masks, layering and procedural patterns. Gate advanced effects such as iridescence and subsurface scattering by device capability.

## Formats
Prioritize GLB/glTF interoperability, then OBJ. Treat FBX and Blender .blend as separate importer projects and only claim compatibility backed by tests on real files. Define a versioned native project container before fixing its extension.

## Tests
Scene/geometry unit tests; material serialization/validation; invalid-input and real importer fixtures; renderer comparisons; touch/inset/orientation tests; CI builds and real-device smoke tests.

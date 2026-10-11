# Architecture

## Goal
Build a mobile-first 3D editor with UI, geometry algorithms, and rendering independently testable and replaceable. Platform-specific code stays at the edges.

## Boundaries
- **DIMITTRESCUI:** navigation, panels, gestures, inspectors, node editors, workspace layout.
- **Session/commands:** selection, tool state, undo/redo, transactional edits.
- **Scene model:** stable IDs, hierarchy, transforms, collections, versioned serialization.
- **Geometry core:** editable vertices/edges/faces, topology operations, UVs, attributes, modifiers, sculpt/deformation data.
- **Material graph:** typed nodes/sockets, parameters, graph validation, procedural evaluation, shader compilation, baking/cache.
- **Renderer adapter:** GPU resources, viewport, camera, lighting, render quality and device capabilities.
- **Import/export:** format-specific adapters with validation and curated fixture tests.
- **Animation/rigging:** skeletons, weights, morphs, IK, timelines and evaluation.
- **Physics/simulation:** isolated solvers, caches, cancellation, stable previews.
- **Asset/project services:** native project files, autosave, recovery, asset metadata and license provenance.

This is a boundary map, not a requirement to create every subsystem immediately.

## Source-of-truth rules
- Scene and mesh data are authoritative; GPU resources are derived.
- Material graphs are authoritative; shaders and baked maps are regenerable caches.
- UI state is not the only place project data lives.
- Every edit command declares affected data and participates in undo/redo.
- Importers validate dimensions, indices, paths, resource sizes and budgets before large allocations.
- Imported scripts, drivers and executable content never run automatically.
- Autosave uses recoverable snapshots/journals and atomic replacement where supported.

## Mobile performance
- Heavy geometry work runs off the UI thread.
- Long jobs expose progress and cancellation when safe.
- Use adaptive preview resolution, LOD, texture budgets and render quality.
- Separate interactive preview quality from final export/render quality.
- Benchmark real devices instead of promising a universal polygon count or frame rate.
- Release GPU resources when scenes and previews are discarded.

## Material graph
Use a versioned renderer-independent graph model with stable node IDs, typed sockets, serializable parameters, explicit resource requirements, and capability-aware compilation. Planned inputs include base color, metallic, roughness, normal, height, opacity, emission, AO, masks, layered blending and procedural patterns. Advanced effects such as thin-film iridescence and subsurface scattering must be capability-gated rather than silently faked.

## File formats
- Define a versioned native project container before committing to an extension.
- Prioritize glTF/GLB interoperability for meshes, PBR materials, skins and animation.
- OBJ is a useful early mesh exchange target with known material/animation limitations.
- Treat FBX and Blender .blend as separate importer projects; only claim support backed by real-file tests.
- Track provenance, license, version, dependencies and required features for assets.

## Test layers
1. Scene and geometry unit tests.
2. Material graph serialization, validation and deterministic compilation tests.
3. Invalid-input and curated real-world importer tests.
4. Renderer image comparisons with device-appropriate tolerances.
5. UI tests for touch targets, orientation, insets and panel state.
6. CI build and smoke tests on a declared Android API/device matrix.

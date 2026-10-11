# Roadmap

Milestones are acceptance gates, not calendar promises. Keep the default branch buildable at each stage.

## Stage 0 — Foundation
- [x] Record product identity and architecture boundaries.
- [x] Document candidate libraries and license review rules.
- [x] Add a minimal Android shell and CI APK artifact workflow.
- [ ] Confirm minimum Android API and real-device test matrix.
- [ ] Add dependency and asset license notices.

**Acceptance:** clean checkout builds in CI; no unverified binary dependency is required.

## Stage 1 — DIMITTRESCUI viewport shell
- [x] Initial visual shell and responsive viewport placeholder.
- [ ] Camera navigation, grid, axes, selection and test mesh.
- [ ] Contextual tool rail and collapsible panels.
- [ ] Portrait/landscape handling and safe-area/system-bar insets.
- [ ] Real-phone touch and memory smoke tests.

## Stage 2 — Scene and command core
- Versioned scene model, stable IDs, transforms and hierarchy.
- Transactional command system with undo/redo.
- Basic primitives and selection.
- Autosave/recovery and deterministic scene serialization.

Acceptance: create/edit/undo/redo/save/reopen a scene without losing object identity.

## Stage 3 — Mesh editing and interchange
- Editable vertices, edges and faces; component selection.
- Move/rotate/scale, extrude, inset, bevel, loop tools and subdivision.
- UV data and basic UV editor.
- GLB/glTF import/export, then OBJ where appropriate.
- Invalid-file and large-file safeguards.

## Stage 4 — Materials and procedural graphs
- Typed graph, sockets, connections, parameters and validation.
- PBR preview, texture baking and cache.
- Searchable library with tags and editable variants.
- Wetness, organic, weathered, optical, ornate and stylized graph recipes.
- Quality tiers and per-device fallbacks.

## Stage 5 — Sculpting and deformation
- Sculpt brushes with spatial acceleration and undo-safe strokes.
- Non-destructive modifier stack where feasible.
- Morph targets, deformation controls and per-face attributes.
- Mobile memory and performance tests.

## Stage 6 — Characters and animation
- Skeletons, skin weights, pose tools, IK, morph animation and timeline.
- Format-specific animation fixture tests.
- Rig evaluation separate from UI and renderer.

## Stage 7 — Physics and advanced workflows
- Rigid bodies, cloth/soft-body experiments, collision and constraints.
- Simulation caches, cancellation, stable tests and reduced-cost previews.
- Expand import compatibility only when format-specific tests justify the claim.

## Every-stage exit checklist
- CI green; no secrets committed; dependency/license changes explained.
- Tests added for new behavior.
- Long operations expose progress and useful errors.
- Touch targets and system insets verified on phone.
- Project files remain compatible or have explicit migrations.

# DIMITTRESCUI design principles

## Visual identity
Inspired by the aristocratic gothic atmosphere associated with Alcina Dimitrescu: luxurious, composed, imposing and slightly ominous. Keep the design original; do not copy game logos, artwork or proprietary UI assets.

Prototype tokens:
- Near-black lacquer and charcoal backgrounds.
- Ivory and warm stone for readable text.
- Deep burgundy/crimson for selection and important state.
- Muted brass/gold as restrained emphasis.
- Strong contrast and status cues that do not rely on color alone.

## Layout
- The 3D viewport is the primary surface, not a background hidden by panels.
- Phone mode prioritizes one active task and compact contextual controls.
- Tablet mode may place the viewport beside an inspector, hierarchy or node graph.
- Panels collapse or switch modes; avoid stacking narrow desktop panels on a phone.
- Respect Android gesture/navigation insets.
- Use comfortable, separated touch targets even when the visible icon is small.
- Provide labels/tooltips for unfamiliar actions; destructive actions need clear affordances.
- Portrait/landscape changes must not reset scene or tool state.

## Interaction
- Camera navigation and selection/transform gestures must not conflict.
- Make active tools and modes obvious.
- Long jobs expose progress, safe cancellation and useful errors.
- Undo/redo must be easy to reach and reflect real edit commands.
- Preserve user work across recoverable failures.

## Material workspace
- Show preview and editable parameters together.
- Node graphs support pan, zoom, fit, selection, socket connections and parameter editing with touch-sized targets.
- Material browser supports search, categories, favorites, tags and editable variants.
- Presets communicate capability requirements and performance tier.
- Include wetness, organic, weathering, optical, ornate and stylized categories—not only generic physical surfaces.

## Reuse boundary
ShofterUI from SomaS3D is a possible reference only. Inspect exact files, dependencies and licenses before copying any code. Do not assume the source contains a complete interface or that its architecture is appropriate for NOBLES-GLOSS.

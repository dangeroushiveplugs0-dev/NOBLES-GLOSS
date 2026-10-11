package com.noblesgloss.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.noblesgloss.app.scene.SceneObject
import com.noblesgloss.app.scene.SceneObjectKind
import io.github.sceneview.SceneView
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.Scale
import io.github.sceneview.math.Size
import io.github.sceneview.node.CubeNode

private val ViewportPanel = Color(0xFF1B191C)
private val ViewportBrass = Color(0xFFC3A16A)
private val ViewportMuted = Color(0xFFB2A6A1)

/**
 * Renders the current scene document using SceneView's declarative node API.
 * Unsupported object kinds currently use a cube proxy until their dedicated renderers exist.
 */
@Composable
fun Viewport3D(
    objects: List<SceneObject>,
    selectedObjectId: String?,
    gizmoMode: TransformMode,
    onGizmoModeChange: (TransformMode) -> Unit,
    onGizmoDrag: (String, String, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(ViewportPanel, RoundedCornerShape(16.dp))
            .border(1.dp, ViewportBrass.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
    ) {
        SceneView(modifier = Modifier.fillMaxSize()) {
            objects.filter { it.visible }.forEach { obj ->
                val transform = obj.transform
                val position = Position(x = transform.x, y = transform.y, z = transform.z)
                val rotation = Rotation(
                    x = transform.rotationX,
                    y = transform.rotationY,
                    z = transform.rotationZ
                )
                val scale = Scale(
                    x = transform.scaleX,
                    y = transform.scaleY,
                    z = transform.scaleZ
                )
                when (obj.kind) {
                    SceneObjectKind.SPHERE -> SphereNode(
                        radius = 0.4f,
                        position = position,
                        rotation = rotation,
                        scale = scale
                    )
                    SceneObjectKind.PLANE -> PlaneNode(
                        size = Size(x = 1.4f, y = 1.4f, z = 0.02f),
                        position = position,
                        rotation = rotation,
                        scale = scale
                    )
                    else -> CubeNode(
                        size = Size(x = 0.8f, y = 0.8f, z = 0.8f),
                        position = position,
                        rotation = rotation,
                        scale = scale
                    )
                }
            }
        }
        Column(modifier = Modifier.align(Alignment.TopStart).padding(8.dp), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(5.dp)) {
            Row(horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)) {
                TransformMode.values().forEach { mode ->
                    Text(
                        text = when (mode) { TransformMode.MOVE -> "↔"; TransformMode.ROTATE -> "⟳"; TransformMode.SCALE -> "⤢" },
                        modifier = Modifier.background(if (gizmoMode == mode) Color(0xFF7C2035) else Color(0xDD1B191C), RoundedCornerShape(8.dp)).border(1.dp, ViewportBrass.copy(alpha = 0.55f), RoundedCornerShape(8.dp)).clickable { onGizmoModeChange(mode) }.padding(horizontal = 10.dp, vertical = 6.dp),
                        color = Color(0xFFF0E8DD), fontSize = 16.sp
                    )
                }
            }
            if (selectedObjectId != null && objects.any { it.id == selectedObjectId }) {
                Text("Drag axes to ${gizmoMode.name}", color = ViewportMuted, fontSize = 8.sp)
                GizmoAxis("X", Color(0xFFE35D6A), true) { onGizmoDrag(selectedObjectId, "X", it) }
                GizmoAxis("Y", Color(0xFF69C58A), false) { onGizmoDrag(selectedObjectId, "Y", it) }
                GizmoAxis("Z", Color(0xFF6CA8FF), true) { onGizmoDrag(selectedObjectId, "Z", it) }
            }
        }
        Text(
            "FILAMENT VIEWPORT · ${objects.count { it.visible }} VISIBLE",
            modifier = Modifier.align(Alignment.BottomStart).padding(12.dp),
            color = ViewportMuted,
            fontSize = 9.sp
        )
        Text(
            "XYZ",
            modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
            color = ViewportBrass,
            fontSize = 10.sp
        )
    }
}

@Composable
private fun GizmoAxis(axis: String, tint: Color, horizontal: Boolean, onDrag: (Float) -> Unit) {
    Row(
        modifier = Modifier.width(92.dp).height(30.dp).background(Color(0xDD1B191C), RoundedCornerShape(8.dp)).border(1.dp, tint.copy(alpha = 0.65f), RoundedCornerShape(8.dp)).pointerInput(axis, horizontal) {
            detectDragGestures { change, dragAmount ->
                change.consume()
                onDrag(if (horizontal) dragAmount.x else -dragAmount.y)
            }
        }.padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        Text(axis, color = tint, fontSize = 12.sp)
        Box(Modifier.weight(1f).height(3.dp).background(tint, RoundedCornerShape(4.dp)))
        Text("↔", color = tint, fontSize = 11.sp)
    }
}

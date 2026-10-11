package com.noblesgloss.app

import android.view.MotionEvent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
 * Renders scene objects and a real, world-space transform gizmo.
 *
 * Gizmo handles are Filament geometry nodes parented to the selected object's pivot. SceneView's
 * ray hit-test identifies the actual 3D handle that starts a drag; no screen-space drag bars are
 * used for transform interaction.
 */
@Composable
fun Viewport3D(
    objects: List<SceneObject>,
    selectedObjectId: String?,
    gizmoMode: TransformMode,
    onGizmoModeChange: (TransformMode) -> Unit,
    onGizmoDragStart: (String) -> Unit,
    onGizmoDrag: (String, String, Float) -> Unit,
    onGizmoDragEnd: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var activeAxis by remember { mutableStateOf<String?>(null) }
    var activeObjectId by remember { mutableStateOf<String?>(null) }
    var lastTouchX by remember { mutableFloatStateOf(0f) }
    var lastTouchY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = modifier
            .background(ViewportPanel, RoundedCornerShape(16.dp))
            .border(1.dp, ViewportBrass.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
    ) {
        SceneView(
            modifier = Modifier.fillMaxSize(),
            onTouchEvent = { event, hitResult ->
                when (event.actionMasked) {
                    MotionEvent.ACTION_DOWN -> {
                        val hitName = hitResult?.nodeOrNull?.name
                        val axis = hitName?.takeIf { it.startsWith("gizmo:") }?.removePrefix("gizmo:")
                        if (axis != null && selectedObjectId != null) {
                            activeAxis = axis
                            activeObjectId = selectedObjectId
                            lastTouchX = event.x
                            lastTouchY = event.y
                            onGizmoDragStart(selectedObjectId)
                            true
                        } else {
                            activeAxis = null
                            activeObjectId = null
                            false
                        }
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val axis = activeAxis
                        val objectId = activeObjectId
                        if (axis != null && objectId != null) {
                            val dx = event.x - lastTouchX
                            val dy = event.y - lastTouchY
                            lastTouchX = event.x
                            lastTouchY = event.y
                            val amount = when (axis) {
                                "Y" -> -dy
                                else -> dx
                            }
                            onGizmoDrag(objectId, axis, amount)
                            true
                        } else false
                    }
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        val axis = activeAxis
                        val objectId = activeObjectId
                        if (axis != null && objectId != null) {
                            onGizmoDragEnd(objectId)
                            activeAxis = null
                            activeObjectId = null
                            true
                        } else false
                    }
                    else -> activeAxis != null
                }
            }
        ) {
            val red = remember(materialLoader) { materialLoader.createUnlitColorInstance(Color(0xFFE35D6A)) }
            val green = remember(materialLoader) { materialLoader.createUnlitColorInstance(Color(0xFF69C58A)) }
            val blue = remember(materialLoader) { materialLoader.createUnlitColorInstance(Color(0xFF6CA8FF)) }
            val center = remember(materialLoader) { materialLoader.createUnlitColorInstance(Color(0xFFF0E8DD)) }

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

            val selected = objects.firstOrNull { it.id == selectedObjectId && it.visible }
            if (selected != null) {
                val p = selected.transform
                // The gizmo inherits only the object's translation so its handles stay a constant
                // world-space size and remain aligned to the scene axes.
                Node(position = Position(p.x, p.y, p.z), isVisible = true) {
                    when (gizmoMode) {
                        TransformMode.MOVE -> {
                            CylinderNode(radius = 0.025f, height = 0.66f, position = Position(y = 0.38f), materialInstance = green, apply = { name = "gizmo:Y" })
                            ConeNode(radius = 0.075f, height = 0.18f, position = Position(y = 0.82f), materialInstance = green, apply = { name = "gizmo:Y" })
                            CylinderNode(radius = 0.025f, height = 0.66f, position = Position(x = 0.38f), rotation = Rotation(z = -90f), materialInstance = red, apply = { name = "gizmo:X" })
                            ConeNode(radius = 0.075f, height = 0.18f, position = Position(x = 0.82f), rotation = Rotation(z = -90f), materialInstance = red, apply = { name = "gizmo:X" })
                            CylinderNode(radius = 0.025f, height = 0.66f, position = Position(z = 0.38f), rotation = Rotation(x = 90f), materialInstance = blue, apply = { name = "gizmo:Z" })
                            ConeNode(radius = 0.075f, height = 0.18f, position = Position(z = 0.82f), rotation = Rotation(x = 90f), materialInstance = blue, apply = { name = "gizmo:Z" })
                        }
                        TransformMode.ROTATE -> {
                            TorusNode(majorRadius = 0.52f, minorRadius = 0.025f, rotation = Rotation(z = 90f), materialInstance = red, apply = { name = "gizmo:X" })
                            TorusNode(majorRadius = 0.52f, minorRadius = 0.025f, materialInstance = green, apply = { name = "gizmo:Y" })
                            TorusNode(majorRadius = 0.52f, minorRadius = 0.025f, rotation = Rotation(x = 90f), materialInstance = blue, apply = { name = "gizmo:Z" })
                        }
                        TransformMode.SCALE -> {
                            CylinderNode(radius = 0.025f, height = 0.66f, position = Position(y = 0.38f), materialInstance = green, apply = { name = "gizmo:Y" })
                            CubeNode(size = Size(0.11f), position = Position(y = 0.78f), materialInstance = green, apply = { name = "gizmo:Y" })
                            CylinderNode(radius = 0.025f, height = 0.66f, position = Position(x = 0.38f), rotation = Rotation(z = -90f), materialInstance = red, apply = { name = "gizmo:X" })
                            CubeNode(size = Size(0.11f), position = Position(x = 0.78f), materialInstance = red, apply = { name = "gizmo:X" })
                            CylinderNode(radius = 0.025f, height = 0.66f, position = Position(z = 0.38f), rotation = Rotation(x = 90f), materialInstance = blue, apply = { name = "gizmo:Z" })
                            CubeNode(size = Size(0.11f), position = Position(z = 0.78f), materialInstance = blue, apply = { name = "gizmo:Z" })
                            CubeNode(size = Size(0.13f), materialInstance = center, apply = { name = "gizmo:UNIFORM" })
                        }
                    }
                }
            }
        }

        Column(
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                TransformMode.values().forEach { mode ->
                    Text(
                        text = when (mode) {
                            TransformMode.MOVE -> "↔"
                            TransformMode.ROTATE -> "⟳"
                            TransformMode.SCALE -> "⤢"
                        },
                        modifier = Modifier
                            .background(if (gizmoMode == mode) Color(0xFF7C2035) else Color(0xDD1B191C), RoundedCornerShape(8.dp))
                            .border(1.dp, ViewportBrass.copy(alpha = 0.55f), RoundedCornerShape(8.dp))
                            .clickable { onGizmoModeChange(mode) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        color = Color(0xFFF0E8DD),
                        fontSize = 16.sp
                    )
                }
            }
            if (selectedObjectId != null && objects.any { it.id == selectedObjectId }) {
                Text("Drag the colored 3D handles", color = ViewportMuted, fontSize = 9.sp)
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

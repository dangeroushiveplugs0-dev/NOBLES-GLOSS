package com.noblesgloss.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
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
 * Renders the current scene document using SceneView's declarative node API.
 * Unsupported object kinds currently use a cube proxy until their dedicated renderers exist.
 */
@Composable
fun Viewport3D(
    objects: List<SceneObject>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(ViewportPanel, RoundedCornerShape(16.dp))
            .border(1.dp, ViewportBrass.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
    ) {
        SceneView(modifier = Modifier.fillMaxSize()) {
            objects.filter { it.visible }.forEach { obj ->
                key(obj.id) {
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

package com.noblesgloss.app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.sceneview.SceneView
import io.github.sceneview.math.Size
import io.github.sceneview.node.CubeNode

private val ViewportPanel = Color(0xFF1B191C)
private val ViewportBrass = Color(0xFFC3A16A)
private val ViewportMuted = Color(0xFFB2A6A1)

/**
 * First renderer integration proof: a real Filament-backed viewport with procedural geometry.
 * Scene data will be moved into the independent scene model in the next milestone.
 */
@Composable
fun Viewport3D(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(ViewportPanel, RoundedCornerShape(16.dp))
            .border(1.dp, ViewportBrass.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
    ) {
        SceneView(modifier = Modifier.fillMaxSize()) {
            CubeNode(size = Size(0.8f, 0.8f, 0.8f))
        }
        Text(
            "FILAMENT VIEWPORT · TEST CUBE",
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
